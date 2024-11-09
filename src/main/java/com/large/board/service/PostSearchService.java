package com.large.board.service;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.dto.response.PageResponse;
import org.springframework.data.domain.Page;

public interface PostSearchService {
    PageResponse<PostDTO> searchPosts(PostSearchRequest postSearchRequest);
}
