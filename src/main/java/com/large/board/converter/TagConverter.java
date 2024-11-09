package com.large.board.converter;

import com.large.board.domain.entity.TagEntity;
import com.large.board.dto.response.TagDTO;

import java.util.List;

public class TagConverter {

    private TagConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static TagDTO toDTO(TagEntity entity) {
        return TagDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .url(entity.getUrl())
                .build();
    }

    public static List<TagDTO> toDTO(List<TagEntity> entities) {
        return entities.stream()
                .map(TagConverter::toDTO)
                .toList();
    }
}
