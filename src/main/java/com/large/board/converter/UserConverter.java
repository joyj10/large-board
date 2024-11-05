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
                .accountId(userEntity.getAccountId())
                .nickname(userEntity.getNickname())
                .status(userEntity.getStatus())
                .isAdmin(userEntity.isAdmin())
                .isWithdrawn(userEntity.getIsWithdrawn())
                .createdDate(userEntity.getCreatedDate())
                .updatedDate(userEntity.getUpdatedDate())
                .build();
    }
}
