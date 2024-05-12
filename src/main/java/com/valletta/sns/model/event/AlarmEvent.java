package com.valletta.sns.model.event;

import com.valletta.sns.model.constant.AlarmType;
import com.valletta.sns.model.dto.AlarmArgs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {

    private Integer receiveUserId;
    private AlarmType alarmType;
    private AlarmArgs args;
}
