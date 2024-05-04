package com.valletta.sns.model;

import com.valletta.sns.model.entity.PostEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostDto {

    private Integer id;
    private String title;
    private String body;
    private UserDto userDto;
//    private UserResponse userDto;
    private Timestamp registerdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static PostDto fromEntity(PostEntity postEntity) {

        return new PostDto(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getBody(),
            UserDto.fromEntity(postEntity.getUser()),
            postEntity.getRegisteredAt(),
            postEntity.getUpdatedAt(),
            postEntity.getDeletedAt()
        );
    }
}
