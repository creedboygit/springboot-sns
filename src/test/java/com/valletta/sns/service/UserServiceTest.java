package com.valletta.sns.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.fixture.UserEntityFixture;
import com.valletta.sns.model.entity.UserEntity;
import com.valletta.sns.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입이_정상적으로_동작하는_경우() {

        String userName = "userName";
        String password = "password";

        // mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());
//        when(userRepository.save(any())).thenReturn(Optional.of(mock(UserEntity.class)));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password, 1));

        assertDoesNotThrow(() -> userService.join(userName, password));
    }

    @Test
    void 회원가입시_userName으로_회원가입한유저가_이미있는경우() {

        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        // mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userRepository.save(any())).thenReturn(Optional.of(fixture));
//        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
        assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    @Test
    void 로그인이_정상적으로_동작하는_경우() {

        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        // mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> userService.login(userName, password));
    }

    @Test
    void 로그인시_userName으로_회원가입한_유저가_없는경우() {

        String userName = "userName";
        String password = "password";

        // mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 로그인시_password가_틀린경우() {

        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        // mocking
        when(userRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(userName, wrongPassword));
        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
