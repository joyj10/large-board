package com.large.board.controller;

import com.large.board.common.utils.SessionUtil;
import com.large.board.dto.request.UserDeleteRequest;
import com.large.board.dto.request.UserLoginRequest;
import com.large.board.dto.request.UserSignUpRequest;
import com.large.board.dto.request.UserUpdatePasswordRequest;
import com.large.board.dto.response.LoginResponse;
import com.large.board.dto.response.UserInfo;
import com.large.board.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody UserSignUpRequest userProfile) {
        userService.register(userProfile);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest,
                                   HttpSession session) {
        UserInfo loginUser = userService.login(userLoginRequest.getUserId(), userLoginRequest.getPassword());

        setSessionBasedOnRole(session, loginUser);

        LoginResponse loginResponse = LoginResponse.success(loginUser);
        return ResponseEntity.ok(loginResponse);
    }

    // 사용자 역할에 따라 세션에 로그인 정보 저장
    private static void setSessionBasedOnRole(HttpSession session, UserInfo loginUser) {
        if (loginUser.isAdmin()) {
            SessionUtil.setLoginAdminId(session, String.valueOf(loginUser.getId()));
        } else {
            SessionUtil.setLoginMemberId(session,  String.valueOf(loginUser.getId()));
        }
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserInfo> getMemberInfo(HttpSession session) {
        Long id = getId(session);

        UserInfo userDto = userService.getUserInfo(id);
        return ResponseEntity.ok(userDto);
    }

    private Long getId(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        if (id == null) {
            id = SessionUtil.getLoginAdminId(session);
        }
        return Long.valueOf(id);
    }

    @PutMapping("/logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("/password")
    public ResponseEntity<UserInfo> updateUserPassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                       HttpSession session) {
        Long id = getId(session);
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        userService.updatePassword(id, beforePassword, afterPassword);
        UserInfo userInfo = userService.getUserInfo(id);

        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteId(@RequestBody UserDeleteRequest userDeleteRequest,
                                         HttpSession session) {
        Long id = getId(session);
        userService.deleteId(id, userDeleteRequest.getPassword());
        return ResponseEntity.noContent().build();
    }

}
