package com.valletta.sns.repository;

import com.valletta.sns.model.entity.CommentEntity;
import com.valletta.sns.model.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update CommentEntity e set e.deletedAt = current_timestamp where e.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);
}
