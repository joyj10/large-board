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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_tag")
@EntityListeners(AuditingEntityListener.class)
public class PostTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private PostEntity postEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private TagEntity tagEntity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;  // 생성 시간

    @LastModifiedDate
    private LocalDateTime updatedDate;  // 수정 시간

    public void setPostEntity(PostEntity postEntity) {
        this.postEntity = postEntity;
    }

    public void setTagEntity(TagEntity tagEntity) {
        this.tagEntity = tagEntity;
    }

    @Builder
    private PostTagEntity(PostEntity postEntity, TagEntity tagEntity) {
        this.postEntity = postEntity;
        this.tagEntity = tagEntity;
    }

    public static PostTagEntity create(PostEntity postEntity, TagEntity tagEntity) {
        return PostTagEntity.builder()
                .postEntity(postEntity)
                .tagEntity(tagEntity)
                .build();
    }

}
