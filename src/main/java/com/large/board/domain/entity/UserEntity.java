package com.large.board.domain.entity;

import com.large.board.common.code.Role;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자를 protected로 제한
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String accountId;              // 사용자 ID

    @Column(nullable = false)
    private String password;            // 비밀번호

    @Column(length = 45)
    private String nickname;            // 닉네임

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isWithdrawn = false; // 탈퇴 여부 (기본값: false)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private UserStatus status;          // 상태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role authority;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  // 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedDate;  // 수정 시간

    // 빌더 패턴
    @Builder
    public UserEntity(String accountId, String password, String nickname, UserStatus status, Role authority) {
        this.accountId = accountId;
        this.password = password;
        this.nickname = nickname;
        this.status = status;
        this.authority = authority;
    }

    // 회원가입 전용 정적 메서드
    public static UserEntity create(String accountId, String password, String nickname) {
        String encryptPassword = PasswordEncryptor.encrypt(password);
        return UserEntity.builder()
                .accountId(accountId)
                .password(encryptPassword)
                .nickname(nickname)
                .authority(Role.USER)
                .status(UserStatus.ACTIVE) // 기본 상태 설정
                .build();
    }

    // 비밀번호 변경
    public void updatePassword(@NotBlank String afterPassword) {
        this.password = PasswordEncryptor.encrypt(afterPassword);
    }

    // 사용자 삭제
    public void delete() {
        this.status = UserStatus.DELETED;
    }


    // UserDetail 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(authority.getKey()));
    }

    public Boolean isAdmin() {
        return authority.getKey().equals("ROLE_ADMIN");
    }

    @Override
    public String getUsername() {
        return this.accountId;
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 lock 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 패스워드 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
