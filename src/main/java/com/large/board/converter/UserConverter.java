package com.large.board.converter;

import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.response.UserInfo;

public class UserConverter {

    private UserConverter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserInfo toUserInfo(UserEntity userEntity) {
        return UserInfo.builder()
                .id(userEntity.getId())
                .userId(userEntity.getUserId())
                .nickname(userEntity.getNickname())
                .status(userEntity.getStatus())
                .isAdmin(userEntity.getIsAdmin())
                .isWithdrawn(userEntity.getIsWithdrawn())
                .createdDate(userEntity.getCreatedDate())
                .updatedDate(userEntity.getUpdatedDate())
                .build();
    }
}
