package com.valletta.sns.repository;

import com.valletta.sns.model.entity.LikeEntity;
import com.valletta.sns.model.entity.PostEntity;
import com.valletta.sns.model.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

//    @Query("SELECT COUNT (*) FROM LikeEntity e where e.post = :postEntity")
//    Integer countByPost(@Param("postEntity") PostEntity postEntity);

    long countByPost(PostEntity postEntity);

    List<LikeEntity> findAllByPost(PostEntity postEntity);
}
