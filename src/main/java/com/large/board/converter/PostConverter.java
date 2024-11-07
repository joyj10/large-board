package com.large.board.converter;

import com.large.board.domain.entity.PostEntity;
import com.large.board.dto.PostDTO;

import java.util.List;

public class PostConverter {
    private PostConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static PostDTO toDto(PostEntity postEntity) {
        return PostDTO.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .contents(postEntity.getContents())
                .isAdmin(postEntity.isAdmin())
                .views(postEntity.getViews())
                .categoryName(postEntity.getCategoryEntity().getName())
                .userName(postEntity.getUserEntity().getUsername())
                .userAccountId(postEntity.getUserEntity().getAccountId())
                .fileId(postEntity.getFileId())
                .createdDate(postEntity.getCreatedDate())
                .updatedDate(postEntity.getUpdatedDate())
                .build();
    }

    public static List<PostDTO> toDto(List<PostEntity> postEntities){
        return postEntities.stream()
                .map(PostConverter::toDto)
                .toList();
    }

}
