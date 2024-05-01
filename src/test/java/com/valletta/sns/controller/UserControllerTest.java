package com.valletta.sns.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valletta.sns.controller.request.UserJoinRequest;
import com.valletta.sns.controller.request.UserLoginRequest;
import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.model.UserDto;
import com.valletta.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @Autowired
    private View error;

    @Test
    public void 회원가입() throws Exception {
        String userName = "userName";
        String password = "password";

        // TODO: mocking
        when(userService.join(userName, password)).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                // TODO: add request body
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.join(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME));

        // TODO: mocking

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                // TODO: add request body
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password)))
            ).andDo(print())
//            .andExpect(status().isConflict());
            .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()));
    }

    @Test
    public void 로그인시_회원가입이안된_userName을_입력할경우_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
            ).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린password를_입력할경우_에러반환() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
            ).andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void 로그인() throws Exception {
        String userName = "userName";
        String password = "password";

        when(userService.login(userName, password)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password)))
            ).andDo(print())
            .andExpect(status().isOk());
    }
}
