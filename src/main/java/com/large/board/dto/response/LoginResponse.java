package com.large.board.dto.response;

import com.large.board.common.code.LoginStatus;
import com.large.board.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {
    @NonNull
    private LoginStatus result;
    private UserInfo userDto;

    private static final LoginResponse FAIL = new LoginResponse(LoginStatus.FAIL);

    public static LoginResponse success(UserInfo userInfo) {
        return new LoginResponse(LoginStatus.SUCCESS, userInfo);
    }
}
