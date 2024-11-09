package com.large.board.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자를 protected로 제한
@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String title;  // 게시물 이름

    @Column(nullable = false, length = 500)
    private String contents;  // 게시물 내용

    @Column(nullable = false)
    private boolean isAdmin = false;  // 관리자 여부

    @Column(nullable = false)
    private int views;  // 조회수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;  // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;  // 사용자

    @Column
    private Long fileId;  // 파일 ID

    @OneToMany(mappedBy = "postEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities = new ArrayList<>();

    @OneToMany(mappedBy = "postEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostTagEntity> postTagEntities = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  // 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedDate;  // 수정 시간

    // 연관 관계 편의 메서드
    public void addComments(List<CommentEntity> commentEntities) {
        for (CommentEntity commentEntity : commentEntities) {
            commentEntity.setPostEntity(this);
            commentEntities.add(commentEntity);
        }
    }

    public void addTag(TagEntity tagEntity) {
        PostTagEntity postTagEntity = PostTagEntity.create(this, tagEntity);
        this.postTagEntities.add(postTagEntity);
        tagEntity.getPostTagEntities().add(postTagEntity);
    }

    public void removeTags() {
        this.postTagEntities = new ArrayList<>();
    }

    @Builder
    private PostEntity(String title, String contents, int views, boolean isAdmin,
                       CategoryEntity categoryEntity, UserEntity userEntity, Long fileId) {
        this.title = title;
        this.isAdmin = isAdmin;
        this.contents = contents;
        this.views = views;
        this.categoryEntity = categoryEntity;
        this.userEntity = userEntity;
        this.fileId = fileId;
    }

    public static PostEntity create(@NotNull String title, @NotNull String contents, boolean isAdmin,
                                    @NotNull CategoryEntity categoryEntity, @NotNull UserEntity userEntity, Long fileId) {
        return PostEntity.builder()
                .title(title)
                .isAdmin(isAdmin)
                .contents(contents)
                .views(0)
                .categoryEntity(categoryEntity)
                .userEntity(userEntity)
                .fileId(fileId)
                .build();
    }

    public void update(@NotNull String title, @NotNull String contents, @NotNull CategoryEntity categoryEntity, Long fileId) {
        this.title = title;
        this.contents = contents;
        this.categoryEntity = categoryEntity;
        this.fileId = fileId;
    }
}
