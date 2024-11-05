package com.large.board.dto.response;

import com.large.board.common.code.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String accountId;
    private String nickname;
    private boolean isAdmin;
    private boolean isWithdrawn;
    private UserStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
