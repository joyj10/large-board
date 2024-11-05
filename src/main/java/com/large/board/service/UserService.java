package com.large.board.service;

import com.large.board.dto.request.UserSignUpRequest;
import com.large.board.dto.response.UserInfo;

public interface UserService {

    void register(UserSignUpRequest userProfile);

    UserInfo login(String accountId, String password);

    boolean isDuplicatedAccountId(String accountId);

    UserInfo getUserInfo(Long id);

    void updatePassword(Long id, String beforePassword, String afterPassword);

    void deleteId(Long id, String password);
}
