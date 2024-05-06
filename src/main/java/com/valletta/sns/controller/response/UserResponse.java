package com.valletta.sns.controller.response;

import com.valletta.sns.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {

    private Integer id;
    private String userName;

    public static UserResponse fromUserDto(UserDto userDto) {

        return new UserResponse(
            userDto.getId(),
            userDto.getUsername()
        );
    }
}
