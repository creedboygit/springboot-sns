package com.valletta.sns.model;

import com.valletta.sns.model.entity.UserEntity;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;

// TODO: implement
@Getter
@Builder
public class UserDto {

    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static UserDto fromEntity(UserEntity userEntity) {
        return UserDto.builder()
            .id(userEntity.getId())
            .userName(userEntity.getUserName())
            .password(userEntity.getPassword())
            .userRole(userEntity.getUserRole())
            .registeredAt(userEntity.getRegisteredAt())
            .updatedAt(userEntity.getUpdatedAt())
            .deletedAt(userEntity.getDeletedAt())
            .build();
    }
}
