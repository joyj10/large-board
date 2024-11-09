package com.large.board.domain.repository;

import com.large.board.domain.entity.PostEntity;
import com.large.board.dto.request.PostSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostRepositoryCustom {
    Page<PostEntity> searchPosts(PostSearchRequest searchRequest);
}
