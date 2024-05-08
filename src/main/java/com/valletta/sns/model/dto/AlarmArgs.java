package com.valletta.sns.model.dto;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class AlarmArgs {

    // 알람을 발생시킨 사람
    private Integer fromUserId;
    private Integer targetId;
}
