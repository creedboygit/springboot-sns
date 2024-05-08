package com.valletta.sns.model.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {

    NEW_COMMENT_ON_POST("new_comment!"),
    NEW_LIKE_ON_POST("new_like!"),
    ;

    private final String alarmText;
}
