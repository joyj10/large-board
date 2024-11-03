package com.large.board.service.impl;

import com.large.board.converter.UserConverter;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.UserDTO;
import com.large.board.dto.request.UserSignUpRequest;
import com.large.board.common.exception.DuplicateIdException;
import com.large.board.common.exception.UnauthorizedException;
import com.large.board.service.UserService;
import com.large.board.common.utils.PasswordEncryptor;
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
        boolean isDuplicatedId = isDuplicatedId(userProfile.getUserId());
        if (isDuplicatedId) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        UserEntity userEntity = UserEntity.register(userProfile.getUserId(), userProfile.getPassword(), userProfile.getNickname());
        userRepository.save(userEntity);
    }

    @Override
    public UserDTO login(String id, String password) {
        UserEntity userEntity = userRepository.findActiveUser(id)
                .orElseThrow(() -> new UsernameNotFoundException("로그인에 실패했습니다."));

        // password 유효성
        if (!PasswordEncryptor.matches(password, userEntity.getPassword())) {
            throw new UnauthorizedException("로그인에 실패했습니다.");
        }

        return UserConverter.toDto(userEntity);
    }

    @Override
    public boolean isDuplicatedId(String id) {
        return userRepository.countByUserId(id) > 0;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        // 사용자 정보 조회 로직 구현
        return null;
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
