package com.valletta.sns.controller.response;

import com.valletta.sns.model.dto.PostDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostResponse {

    private Integer id;
    private String title;
    private String body;
    private UserResponse userResponse;
    private Timestamp registerdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostResponse fromPostDto(PostDto postDto) {

        return new PostResponse(
            postDto.getId(),
            postDto.getTitle(),
            postDto.getBody(),
            UserResponse.fromUserDto(postDto.getUserDto()),
            postDto.getRegisterdAt(),
            postDto.getUpdatedAt(),
            postDto.getDeletedAt()
        );
    }
}
