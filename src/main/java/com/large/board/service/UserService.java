package com.large.board.service;

import com.large.board.dto.request.UserSignUpRequest;
import com.large.board.dto.response.UserInfo;

public interface UserService {

    void register(UserSignUpRequest userProfile);

    UserInfo login(String id, String password);

    boolean isDuplicatedUserId(String userId);

    UserInfo getUserInfo(String id);

    void updatePassword(String id, String beforePassword, String afterPassword);

    void deleteId(String id, String password);
}
