package com.valletta.sns.service;

import com.valletta.sns.exception.ErrorCode;
import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.model.UserDto;
import com.valletta.sns.model.UserRole;
import com.valletta.sns.model.entity.UserEntity;
import com.valletta.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserDto join(String userName, String password) {

        // 회원가입하려는 userName으로 회원가입된 user가 있는지
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplaicated", userName));
        });

        // 회원가입 진행 = user를 등록
//        userRepository.save(new UserEntity());
        UserEntity userEntity = userRepository.save(UserEntity.of(userName, encoder.encode(password), UserRole.USER));

        return UserDto.fromEntity(userEntity);
    }

    // TODO: implement
    public String login(String userName, String password) {

        // 회원가입 여부 체크
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        // 비밀번호 체크
        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "");
        }

        // 토큰 생성

        return "";
    }
}
