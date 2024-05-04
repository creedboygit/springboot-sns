package com.valletta.sns.fixture;

import com.valletta.sns.model.entity.PostEntity;
import com.valletta.sns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String userName, Integer postId, Integer userId) {

        UserEntity user = UserEntity.builder()
//            .id(1)
            .id(userId)
            .userName(userName)
            .build();

        return PostEntity.builder()
            .user(user)
            .id(postId)
            .build();
    }
}
