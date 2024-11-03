package com.large.board.converter;

import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.UserDTO;

public class UserConverter {

    private UserConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserDTO toDto(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .userId(userEntity.getUserId())
                .nickname(userEntity.getNickname())
                .isAdmin(userEntity.getIsAdmin())
                .build();
    }
}
