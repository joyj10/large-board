package com.large.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String accountId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
