package com.valletta.sns.controller;

import com.valletta.sns.controller.request.UserJoinRequest;
import com.valletta.sns.controller.response.Response;
import com.valletta.sns.controller.response.UserJoinResponse;
import com.valletta.sns.model.UserDto;
import com.valletta.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDto userDto = userService.join(request.getUserName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUserDto(userDto));
    }
}
