package com.valletta.sns.controller.response;

import com.valletta.sns.model.UserDto;
import com.valletta.sns.model.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserJoinResponse {

    private Integer id;
    private String userName;
    private UserRole userRole;

    public static UserJoinResponse fromUserDto(UserDto userDto) {

        return UserJoinResponse.builder()
            .id(userDto.getId())
            .userName(userDto.getUsername())
            .userRole(userDto.getUserRole())
            .build();
    }
}
