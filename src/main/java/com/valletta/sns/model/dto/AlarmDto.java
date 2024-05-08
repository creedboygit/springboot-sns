package com.valletta.sns.model.dto;

import com.valletta.sns.model.constant.AlarmType;
import com.valletta.sns.model.entity.AlarmEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AlarmDto {

    private Integer id;
    private UserDto userDto;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;

    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmDto fromEntity(AlarmEntity alarmEntity) {

        return AlarmDto.builder()
            .id(alarmEntity.getId())
            .userDto(UserDto.fromEntity(alarmEntity.getUser()))
            .alarmType(alarmEntity.getAlarmType())
            .alarmArgs(alarmEntity.getAlarmArgs())
            .registeredAt(alarmEntity.getRegisteredAt())
            .updatedAt(alarmEntity.getUpdatedAt())
            .deletedAt(alarmEntity.getDeletedAt())
            .build();
    }
}
