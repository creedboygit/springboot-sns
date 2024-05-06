package com.valletta.sns.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
//@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"comment\"", indexes = {
    @Index(name = "post_id_idx", columnList = "post_id")
})
@SQLDelete(sql = "UPDATE \"comment\" SET deleted_at = NOW() where id = ?")
@SQLRestriction("deleted_at is NULL")
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "comment")
    private String comment;

    @Column(name = "register_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void setRegisteredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void setUpdatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static CommentEntity of(UserEntity userEntity, PostEntity postEntity, String comment) {
        return CommentEntity.builder()
            .user(userEntity)
            .post(postEntity)
            .comment(comment)
            .build();
    }
}
