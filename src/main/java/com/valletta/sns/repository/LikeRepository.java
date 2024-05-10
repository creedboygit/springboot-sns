package com.valletta.sns.repository;

import com.valletta.sns.model.entity.LikeEntity;
import com.valletta.sns.model.entity.PostEntity;
import com.valletta.sns.model.entity.UserEntity;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

//    @Query("SELECT COUNT (*) FROM LikeEntity e where e.post = :postEntity")
//    Integer countByPost(@Param("postEntity") PostEntity postEntity);

    long countByPost(PostEntity postEntity);

//    List<LikeEntity> findAllByPost(PostEntity postEntity);

    @Transactional
    @Modifying
    @Query("update LikeEntity e SET e.deletedAt = current_timestamp where e.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);
}
