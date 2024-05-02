package com.valletta.sns.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.fixture.PostEntityFixture;
import com.valletta.sns.fixture.UserEntityFixture;
import com.valletta.sns.model.entity.PostEntity;
import com.valletta.sns.model.entity.UserEntity;
import com.valletta.sns.repository.PostRepository;
import com.valletta.sns.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void 포스트작성이_성공한경우() {

        String title = "제목";
        String body = "내용";
        String userName = "creed";

        // mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(userRepository.save(any())).thenReturn(mock(PostEntity.class));

        assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @Test
    void 포스트작성시_요청한user가_존재하지않는경우() {

        String title = "제목";
        String body = "내용";
        String userName = "creed";

        // mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트수정이_성공한경우() {

        String title = "제목";
        String body = "내용";
        String userName = "creed";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId);
        UserEntity userEntity = postEntity.getUser();

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));
    }

    @Test
    void 포스트수정시_포스트가존재하지않는경우() {

        String title = "제목";
        String body = "내용";
        String userName = "creed";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId);
        UserEntity userEntity = postEntity.getUser();

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트수정시_권한이없는경우() {

        String title = "제목";
        String body = "내용";
        String userName = "creed";
        Integer postId = 1;

        PostEntity postEntity = PostEntityFixture.get(userName, postId);
        UserEntity writer = UserEntityFixture.get("userName1", "password");

        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }
}