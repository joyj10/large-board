package com.large.board.domain.entity;

import com.large.board.common.code.UserStatus;
import com.large.board.common.exception.BadRequestException;
import com.large.board.common.utils.PasswordEncryptor;
import com.large.board.service.CategoryService;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자를 protected로 제한
@Entity
@Table(name = "category")
@EntityListeners(AuditingEntityListener.class)
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String name;            // 이름

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted = false;  // 삭제 여부 (기본값: false)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  // 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedDate;  // 수정 시간

    @Builder
    private CategoryEntity(String name, boolean isDeleted) {
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public static CategoryEntity register(String name) {
        validName(name);
        return CategoryEntity.builder()
                .name(name)
                .isDeleted(false)
                .build();
    }

    private static void validName(String name) {
        if (Strings.isBlank(name)) {
            throw new BadRequestException("카테고리 명은 필수값입니다.");
        }
    }

    public void update(String name) {
        validName(name);
        this.name = name;
    }

    public void delete() {
        if (this.isDeleted) {
            throw new BadRequestException("이미 삭제된 카테고리입니다.");
        }

        this.isDeleted = true;
    }
}
