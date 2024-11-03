package com.large.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdatePasswordRequest {
    @NotBlank(message = "기존 비밀번호는 필수입니다.")
    private String beforePassword;

    @NotBlank(message = "새로운 비밀번호는 필수입니다.")
    private String afterPassword;
}
