package com.large.board.domain.repository;

import com.large.board.domain.entity.PostEntity;
import com.large.board.dto.request.PostSearchRequest;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostEntity> searchPosts(PostSearchRequest searchRequest);
}
