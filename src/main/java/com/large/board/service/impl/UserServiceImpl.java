package com.large.board.service.impl;

import com.large.board.common.exception.DuplicateIdException;
import com.large.board.common.exception.UnauthorizedException;
import com.large.board.common.utils.PasswordEncryptor;
import com.large.board.converter.UserConverter;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.request.UserSignUpRequest;
import com.large.board.dto.response.UserInfo;
import com.large.board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void register(UserSignUpRequest userProfile) {
        boolean isDuplicatedId = isDuplicatedUserId(userProfile.getUserId());
        if (isDuplicatedId) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        UserEntity userEntity = UserEntity.register(userProfile.getUserId(), userProfile.getPassword(), userProfile.getNickname());
        userRepository.save(userEntity);
    }

    @Override
    public UserInfo login(String userId, String password) {
        UserEntity userEntity = userRepository.findActiveUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("로그인에 실패했습니다."));

        validPassword(password, userEntity, "로그인에 실패했습니다.");

        return UserConverter.toUserInfo(userEntity);
    }

    // password 유효성
    private static void validPassword(String password, UserEntity userEntity, String message) {
        if (!PasswordEncryptor.matches(password, userEntity.getPassword())) {
            throw new UnauthorizedException(message);
        }
    }

    @Override
    public boolean isDuplicatedUserId(String userId) {
        return userRepository.countByUserId(userId) > 0;
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        UserEntity userEntity = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("활성화된 사용자 정보가 없습니다."));
        return UserConverter.toUserInfo(userEntity);
    }

    @Override
    @Transactional
    public void updatePassword(Long id, String beforePassword, String afterPassword) {
        UserEntity userEntity = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("활성화된 사용자 정보가 없습니다."));

        validPassword(beforePassword, userEntity, "비밀번호가 일치하지 않습니다.");

        // 새로운 비밀번호 변경
        userEntity.updatePassword(afterPassword);
    }

    @Override
    @Transactional
    public void deleteId(Long id, String password) {
        // 계정 삭제 로직 구현
    }
}
