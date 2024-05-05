package com.valletta.sns.repository;

import com.valletta.sns.model.entity.LikeEntity;
import com.valletta.sns.model.entity.PostEntity;
import com.valletta.sns.model.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

//    LikeEntity findByUserAndPost(UserEntity user, PostEntity post);
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}