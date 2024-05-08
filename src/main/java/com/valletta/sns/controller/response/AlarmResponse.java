package com.valletta.sns.controller.response;

import com.valletta.sns.model.constant.AlarmType;
import com.valletta.sns.model.dto.AlarmArgs;
import com.valletta.sns.model.dto.AlarmDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AlarmResponse {

    private Integer id;
//    private UserResponse userResponse;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String alarmText;

    private Timestamp registerdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarmDto(AlarmDto alarmDto) {

        return AlarmResponse.builder()
            .id(alarmDto.getId())
//            .userResponse(UserResponse.fromUserDto(alarmDto.getUserDto()))
            .alarmType(alarmDto.getAlarmType())
            .alarmArgs(alarmDto.getAlarmArgs())
            .alarmText(alarmDto.getAlarmType().getAlarmText())
            .registerdAt(alarmDto.getRegisteredAt())
            .updatedAt(alarmDto.getUpdatedAt())
            .deletedAt(alarmDto.getDeletedAt())
            .build();
    }
}
