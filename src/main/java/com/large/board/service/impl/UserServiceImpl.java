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
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void register(UserSignUpRequest userProfile) {
        boolean isDuplicatedId = isDuplicatedAccountId(userProfile.getAccountId());
        if (isDuplicatedId) {
            throw new DuplicateIdException("중복된 아이디입니다.");
        }
        UserEntity userEntity = UserEntity.create(userProfile.getAccountId(), userProfile.getPassword(), userProfile.getNickname());
        userRepository.save(userEntity);
    }

    @Override
    public UserEntity login(String accountId, String password) {
        UserEntity userEntity = userRepository.findActiveUserByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("로그인에 실패했습니다."));

        validPassword(password, userEntity, "로그인에 실패했습니다.");

        return userEntity;
    }

    // password 유효성
    private static void validPassword(String password, UserEntity userEntity, String message) {
        if (!PasswordEncryptor.matches(password, userEntity.getPassword())) {
            throw new UnauthorizedException(message);
        }
    }

    @Override
    public boolean isDuplicatedAccountId(String accountId) {
        return userRepository.countByAccountId(accountId) > 0;
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        UserEntity userEntity = getUserEntity(id);
        return UserConverter.toUserInfo(userEntity);
    }

    @Transactional
    @Override
    public void updatePassword(Long id, String beforePassword, String afterPassword) {
        UserEntity userEntity = getUserEntity(id);

        validPassword(beforePassword, userEntity, "변경 불가능합니다.");

        // 새로운 비밀번호 변경
        userEntity.updatePassword(afterPassword);
    }

    private UserEntity getUserEntity(Long id) {
        return userRepository.findActiveUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("활성화된 사용자 정보가 없습니다."));
    }

    @Transactional
    @Override
    public void deleteId(Long id, String password) {
        UserEntity userEntity = getUserEntity(id);
        validPassword(password, userEntity, "삭제를 진행할 수 없습니다.");
        userEntity.delete();
    }
}
