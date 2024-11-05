package com.large.board.controller;

import com.large.board.common.utils.SessionUtil;
import com.large.board.domain.entity.UserEntity;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody UserSignUpRequest userProfile) {
        userService.register(userProfile);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest,
                                               HttpSession session) {
        String accountId = userLoginRequest.getAccountId();
        String password = userLoginRequest.getPassword();

        UserInfo loginUser = userService.login(userLoginRequest.getAccountId(), userLoginRequest.getPassword());

        // Authentication 객체 생성 및 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(accountId, password));

        // 인증이 성공하면 세션에 사용자 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 사용자 정보를 세션에 저장
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = ((UserEntity) userDetails).getId(); // UserEntity에서 ID 가져오기
        session.setAttribute("userId", userId); // 세션에 사용자 ID 저장
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok(LoginResponse.success(loginUser));
    }

    @GetMapping("/my-info")
    public ResponseEntity<UserInfo> getMyInfo(HttpSession session) {
        // 세션에서 userId를 가져옵니다.
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserInfo userDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("/password")
    public ResponseEntity<UserInfo> updateUserPassword(@RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                       HttpSession session) {
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        Long userId = (Long) session.getAttribute("userId");
        userService.updatePassword(userId, beforePassword, afterPassword);
        UserInfo userInfo = userService.getUserInfo(userId);

        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteId(@RequestBody UserDeleteRequest userDeleteRequest,
                                         HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        userService.deleteId(userId, userDeleteRequest.getPassword());
        return ResponseEntity.noContent().build();
    }
}
