package com.large.board.controller;

import com.large.board.dto.UserDTO;
import com.large.board.dto.request.UserLoginRequest;
import com.large.board.dto.request.UserSignUpRequest;
import com.large.board.dto.response.LoginResponse;
import com.large.board.service.impl.UserServiceImpl;
import com.large.board.common.utils.SessionUtil;
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
        UserDTO loginUser = userService.login(userLoginRequest.getUserId(), userLoginRequest.getPassword());

        setSessionBasedOnRole(session, loginUser);

        LoginResponse loginResponse = LoginResponse.success(loginUser);
        return ResponseEntity.ok(loginResponse);
    }

    // 사용자 역할에 따라 세션에 로그인 정보 저장
    private static void setSessionBasedOnRole(HttpSession session, UserDTO loginUser) {
        if (loginUser.isAdmin()) {
            SessionUtil.setLoginAdminId(session, loginUser.getUserId());
        } else {
            SessionUtil.setLoginMemberId(session, loginUser.getUserId());
        }
    }

}
