package com.valletta.sns.controller.response;

import com.valletta.sns.model.dto.CommentDto;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {

    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentResponse fromCommentDto(CommentDto commentDto) {

        return CommentResponse.builder()
            .id(commentDto.getId())
            .comment(commentDto.getComment())
            .userName(commentDto.getUserName())
            .postId(commentDto.getPostId())
            .registeredAt(commentDto.getRegisteredAt())
            .updatedAt(commentDto.getUpdatedAt())
            .deletedAt(commentDto.getDeletedAt())
            .build();
    }
}
