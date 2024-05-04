package com.valletta.sns.fixture;

import com.valletta.sns.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password, Integer userId) {

//        UserEntity result = new UserEntity();

        return UserEntity.builder()
            .id(userId)
            .userName(userName)
            .password(password)
            .build();
    }
}
