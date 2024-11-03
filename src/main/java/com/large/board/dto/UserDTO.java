package com.large.board.dto;

import com.large.board.common.code.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
