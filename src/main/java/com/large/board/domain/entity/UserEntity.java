package com.large.board.domain.entity;

import com.large.board.common.code.UserStatus;
import com.large.board.common.utils.PasswordEncryptor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자를 protected로 제한
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String userId;              // 사용자 ID

    @Column(nullable = false, length = 255)
    private String password;            // 비밀번호

    @Column(length = 45)
    private String nickname;            // 닉네임

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isAdmin = false;    // 관리자 여부 (기본값: false)

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isWithdrawn = false; // 탈퇴 여부 (기본값: false)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private UserStatus status;          // 상태

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  // 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedDate;  // 수정 시간

    // 빌더 패턴
    @Builder
    public UserEntity(String userId, String password, String nickname, UserStatus status) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
    }

    // 회원가입 전용 정적 메서드
    public static UserEntity register(String userId, String password, String nickname) {
        String encryptPassword = PasswordEncryptor.encrypt(password);
        return UserEntity.builder()
                .userId(userId)
                .password(encryptPassword)
                .nickname(nickname)
                .status(UserStatus.ACTIVE) // 기본 상태 설정
                .build();
    }

    // 비밀번호 변경
    public void updatePassword(@NotBlank String afterPassword) {
        String encryptPassword = PasswordEncryptor.encrypt(afterPassword);
        this.password = encryptPassword;
    }
}
