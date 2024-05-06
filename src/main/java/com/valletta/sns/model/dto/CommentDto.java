package com.valletta.sns.model.dto;

import com.valletta.sns.model.entity.CommentEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentDto {

    private Integer id;
    private String userName;
    private Integer postId;
    private String comment;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static CommentDto fromEntity(CommentEntity commentEntity) {

        return CommentDto.builder()
            .id(commentEntity.getId())
            .userName(commentEntity.getUser().getUserName())
            .postId(commentEntity.getPost().getId())
            .comment(commentEntity.getComment())
            .registeredAt(commentEntity.getRegisteredAt())
            .updatedAt(commentEntity.getUpdatedAt())
            .deletedAt(commentEntity.getDeletedAt())
            .build();
    }
}
