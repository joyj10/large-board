package com.large.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserSignUpRequest {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Size(max = 45, message = "사용자 ID는 45자 이내 여야 합니다.")
    private String accountId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 최소 8자 이상 최대 20자 이내 여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 45, message = "닉네임은 45자 이내여야 합니다.")
    private String nickname;
}
