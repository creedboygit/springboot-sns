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
@Table(name = "\"like\"", indexes = {
    @Index(name = "user_id_idx", columnList = "user_id"),
    @Index(name = "post_id_idx", columnList = "post_id")
})
@SQLDelete(sql = "UPDATE \"like\" SET deleted_at = NOW() where id = ?")
@SQLRestriction("deleted_at is NULL")
@Entity
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

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

    public static LikeEntity of(UserEntity userEntity, PostEntity postEntity) {
        return LikeEntity.builder()
            .user(userEntity)
            .post(postEntity)
            .build();
    }
}
