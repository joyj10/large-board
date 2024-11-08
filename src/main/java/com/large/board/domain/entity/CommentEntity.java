package com.large.board.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity postEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentCommentEntity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  // 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedDate;  // 수정 시간

    public void setPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }


    @Builder
    private CommentEntity(String contents, PostEntity postEntity, UserEntity userEntity, CommentEntity parentCommentEntity) {
        this.contents = contents;
        this.postEntity = postEntity;
        this.userEntity = userEntity;
        this.parentCommentEntity = parentCommentEntity;
    }

    public static CommentEntity create(@NotBlank String contents,
                                       @NotNull PostEntity postEntity,
                                       @NotNull UserEntity userEntity,
                                       CommentEntity parentCommentEntity) {
        return CommentEntity.builder()
                .contents(contents)
                .postEntity(postEntity)
                .userEntity(userEntity)
                .parentCommentEntity(parentCommentEntity)
                .build();
    }

    public void update(@NotBlank String contents) {
        this.contents = contents;
    }
}
