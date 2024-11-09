package com.large.board.controller;

import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.request.UserDeleteRequest;
import com.large.board.dto.request.UserSignUpRequest;
import com.large.board.dto.request.UserUpdatePasswordRequest;
import com.large.board.dto.response.UserInfo;
import com.large.board.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/my-info")
    public ResponseEntity<UserInfo> getMyInfo(@AuthenticationPrincipal UserEntity userEntity) {
        Long userId = userEntity.getId();
        UserInfo userDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDto);
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
