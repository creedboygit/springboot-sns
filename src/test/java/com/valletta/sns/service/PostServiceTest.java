package com.valletta.sns.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
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
}