package com.large.board.service;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import org.springframework.data.domain.Page;

public interface PostSearchService {
    Page<PostDTO> searchPosts(PostSearchRequest postSearchRequest);
}
