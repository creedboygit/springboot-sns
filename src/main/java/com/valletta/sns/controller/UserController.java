package com.valletta.sns.controller;

import com.valletta.sns.controller.request.UserJoinRequest;
import com.valletta.sns.controller.request.UserLoginRequest;
import com.valletta.sns.controller.response.AlarmResponse;
import com.valletta.sns.controller.response.Response;
import com.valletta.sns.controller.response.UserJoinResponse;
import com.valletta.sns.controller.response.UserLoginResponse;
import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.model.dto.UserDto;
import com.valletta.sns.service.AlarmService;
import com.valletta.sns.service.UserService;
import com.valletta.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000") // 허용할 출처
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AlarmService alarmService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto userDto = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUserDto(userDto));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
//        UserDto user = (UserDto) authentication.getPrincipal();
//        return Response.success(userService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromAlarmDto));
        UserDto user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarmDto));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        log.info("subscribe");
        UserDto user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDto.class).orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to User class failed"));
        return alarmService.connectAlarm(user.getId());
    }
}
