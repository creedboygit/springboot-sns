package com.valletta.sns.service;

import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.model.constant.AlarmType;
import com.valletta.sns.model.dto.AlarmArgs;
import com.valletta.sns.model.entity.AlarmEntity;
import com.valletta.sns.model.entity.UserEntity;
import com.valletta.sns.repository.AlarmRepository;
import com.valletta.sns.repository.EmitterRepository;
import com.valletta.sns.repository.UserRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String ALARM_NAME = "alarm";
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

//    public void send(Integer alarmId, Integer userId) {
    public void send(AlarmType alarmType, AlarmArgs args, Integer receiveUserId) {

        UserEntity userEntity = userRepository.findById(receiveUserId).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", receiveUserId)));

        // alarm save
        AlarmEntity alarmEntity = alarmRepository.save(AlarmEntity.of(userEntity, alarmType, args));

        emitterRepository.get(receiveUserId).ifPresentOrElse(
            sseEmitter -> {
                try {
                    log.info("alarm sended");
                    sseEmitter.send(SseEmitter.event()
                        .id(alarmEntity.getId().toString())
                        .name(ALARM_NAME)
                        .data("new alarm"));
                } catch (IOException e) {
                    emitterRepository.delete(receiveUserId);
                    throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR, "alarm connect error 1");
                }
            },
            () -> log.info("No emitter founded")
        );
    }

    public SseEmitter connectAlarm(Integer userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            log.info("connect send");
            sseEmitter.send(SseEmitter.event()
                .id("id")
                .name("connect")
                .data("connect completed"));
        } catch (IOException e) {
            throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR, "alarm connect error 2");
        }

        return sseEmitter;
    }
}
