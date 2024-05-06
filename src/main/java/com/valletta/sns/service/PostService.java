package com.valletta.sns.service;

import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.model.PostDto;
import com.valletta.sns.model.entity.LikeEntity;
import com.valletta.sns.model.entity.PostEntity;
import com.valletta.sns.model.entity.UserEntity;
import com.valletta.sns.repository.LikeRepository;
import com.valletta.sns.repository.PostRepository;
import com.valletta.sns.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public void create(String title, String body, String userName) {

        // user find
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s now founded", userName)));

        // post save
        postRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public PostDto modify(String title, String body, String userName, Integer postId) {

        // user find
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s now founded", userName)));

        // post exist
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntity.updatePost(title, body);
//        postEntity.setTitle(title);
//        postEntity.setBody(body);

//        return PostDto.fromEntity(postRepository.save(postEntity));
        return PostDto.fromEntity(postRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {

        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // post exist
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postRepository.delete(postEntity);
    }

    public Page<PostDto> list(Pageable pageable) {

        return postRepository.findAll(pageable).map(PostDto::fromEntity);
    }

    public Page<PostDto> my(String userName, Pageable pageable) {

        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        return postRepository.findAllByUser(userEntity, pageable).map(PostDto::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {

        // post exist
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        // check liked -> throw
        likeRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already liked post %d", userName, postId));
        });

        // like save
        likeRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    public int likeCount(Integer postId) {

        // post exist
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        List<LikeEntity> likeEntities = likeRepository.findAllByPost(postEntity);
        return likeEntities.size();
    }
}
