package com.large.board.service;

import com.large.board.dto.UserDTO;
import com.large.board.dto.request.UserSignUpRequest;

public interface UserService {

    void register(UserSignUpRequest userProfile);

    UserDTO login(String id, String password);

    boolean isDuplicatedId(String id);

    UserDTO getUserInfo(String userId);

    void updatePassword(String id, String beforePassword, String afterPassword);

    void deleteId(String id, String password);
}
