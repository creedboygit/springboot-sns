package com.valletta.sns.service;

import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.model.constant.AlarmType;
import com.valletta.sns.model.dto.AlarmArgs;
import com.valletta.sns.model.dto.CommentDto;
import com.valletta.sns.model.dto.PostDto;
import com.valletta.sns.model.entity.AlarmEntity;
import com.valletta.sns.model.entity.CommentEntity;
import com.valletta.sns.model.entity.LikeEntity;
import com.valletta.sns.model.entity.PostEntity;
import com.valletta.sns.model.entity.UserEntity;
import com.valletta.sns.repository.AlarmRepository;
import com.valletta.sns.repository.CommentRepository;
import com.valletta.sns.repository.LikeRepository;
import com.valletta.sns.repository.PostRepository;
import com.valletta.sns.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public void create(String title, String body, String userName) {

        UserEntity userEntity = getUserEntityOrException(userName);

        // post save
        postRepository.save(PostEntity.of(title, body, userEntity));
    }

    @Transactional
    public PostDto modify(String title, String body, String userName, Integer postId) {

        UserEntity userEntity = getUserEntityOrException(userName);
        PostEntity postEntity = getPostEntityOrException(postId);

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntity.updatePost(title, body);
        return PostDto.fromEntity(postRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {

        UserEntity userEntity = getUserEntityOrException(userName);
        PostEntity postEntity = getPostEntityOrException(postId);

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

        UserEntity userEntity = getUserEntityOrException(userName);

        return postRepository.findAllByUser(userEntity, pageable).map(PostDto::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String userName) {

        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        // check liked -> throw
        likeRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already liked post %d", userName, postId));
        });

        // like save
        likeRepository.save(LikeEntity.of(userEntity, postEntity));

        // alarm save
        alarmRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public int likeCount(Integer postId) {

        PostEntity postEntity = getPostEntityOrException(postId);

        return likeRepository.countByPost(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {

        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        // comment save
        commentRepository.save(CommentEntity.of(userEntity, postEntity, comment));

        // alarm save
        alarmRepository.save(AlarmEntity.of(postEntity.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postEntity.getId())));
    }

    public Page<CommentDto> getComments(Integer postId, Pageable pageable) {

        PostEntity postEntity = getPostEntityOrException(postId);

        return commentRepository.findAllByPost(postEntity, pageable).map(CommentDto::fromEntity);
    }

    /**
     * post exist
     */
    private PostEntity getPostEntityOrException(Integer postId) {

        return postRepository.findById(postId).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

    /**
     * user exist
     */
    private UserEntity getUserEntityOrException(String userName) {

        return userRepository.findByUserName(userName).orElseThrow(() ->
            new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }
}
