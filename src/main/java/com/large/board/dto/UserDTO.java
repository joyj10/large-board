package com.large.board.dto;

import com.large.board.domain.code.UserStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private boolean isAdmin;
    private boolean isWithdrawn;
    private UserStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
