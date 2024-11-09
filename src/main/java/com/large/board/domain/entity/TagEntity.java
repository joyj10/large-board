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
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tag")
@EntityListeners(AuditingEntityListener.class)
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "url", nullable = false, length = 45)
    private String url;

    @OneToMany(mappedBy = "tagEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostTagEntity> postTagEntities = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  // 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedDate;  // 수정 시간

    @Builder
    private TagEntity(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static TagEntity create(String name, String url) {
        return TagEntity.builder()
                .name(name)
                .url(url)
                .build();
    }

    public void addPostTag(PostEntity postEntity) {
        PostTagEntity postTagEntity = PostTagEntity.create(postEntity, this);
        postTagEntities.add(postTagEntity);
        postEntity.getPostTagEntities().add(postTagEntity);
    }

    public void update(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
