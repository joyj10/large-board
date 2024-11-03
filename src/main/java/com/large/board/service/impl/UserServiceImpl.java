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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void register(UserSignUpRequest userProfile) {
        boolean isDuplicatedId = isDuplicatedUserId(userProfile.getUserId());
        if (isDuplicatedId) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        UserEntity userEntity = UserEntity.register(userProfile.getUserId(), userProfile.getPassword(), userProfile.getNickname());
        userRepository.save(userEntity);
    }

    @Override
    public UserInfo login(String id, String password) {
        UserEntity userEntity = userRepository.findActiveUserByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("로그인에 실패했습니다."));

        // password 유효성
        if (!PasswordEncryptor.matches(password, userEntity.getPassword())) {
            throw new UnauthorizedException("로그인에 실패했습니다.");
        }

        return UserConverter.toUserInfo(userEntity);
    }

    @Override
    public boolean isDuplicatedUserId(String userId) {
        return userRepository.countByUserId(userId) > 0;
    }

    @Override
    public UserInfo getUserInfo(String id) {
        UserEntity userEntity = userRepository.findActiveUserById(Long.valueOf(id))
                .orElseThrow(() -> new UsernameNotFoundException("활성화된 사용자 정보가 없습니다."));
        return UserConverter.toUserInfo(userEntity);
    }

    @Override
    public void updatePassword(String id, String beforePassword, String afterPassword) {
        // 비밀번호 업데이트 로직 구현
    }

    @Override
    public void deleteId(String id, String password) {
        // 계정 삭제 로직 구현
    }
}
