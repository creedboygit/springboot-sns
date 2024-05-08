package com.valletta.sns.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Embeddable
public class AlarmArgs {

    // 알람을 발생시킨 사람
    private Integer fromUserId;
    private Integer targetId;
}
