package com.large.board.controller;

import com.large.board.common.utils.SessionUtil;
import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.request.UserLoginRequest;
import com.large.board.dto.response.LoginResponse;
import com.large.board.dto.response.UserInfo;
import com.large.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest userLoginRequest,
                                               HttpSession session) {
        String accountId = userLoginRequest.getAccountId();
        String password = userLoginRequest.getPassword();

        UserEntity loginUser = userService.login(accountId, password);

        // SecurityContext 인증 저장
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 사용자 정보를 세션에 저장
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = ((UserEntity) userDetails).getId();
        session.setAttribute("userId", userId); // 세션에 사용자 ID 저장
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok(LoginResponse.success(new UserInfo()));
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }
}
