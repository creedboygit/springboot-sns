package com.valletta.sns.service;

import com.valletta.sns.exception.SnsApplicationException;
import com.valletta.sns.model.User;
import com.valletta.sns.model.entity.UserEntity;
import com.valletta.sns.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // TODO: implement
    public User join(String userName, String password) {

        // 회원가입하려는 userName으로 회원가입된 user가 있는지
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);

        // 회원가입 진행 = user를 등록
        userRepository.save(new UserEntity());

        return new User();
    }

    // TODO: implement
    public String login(String userName, String password) {

        // 회원가입 여부 체크
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(SnsApplicationException::new);

        // 비밀번호 체크
        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException();
        }

        // 토큰 생성

        return "";
    }
}
