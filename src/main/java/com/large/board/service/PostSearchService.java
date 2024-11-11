package com.large.board.service;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.dto.response.PageResponse;

public interface PostSearchService {
    PageResponse<PostDTO> searchPosts(PostSearchRequest postSearchRequest);
}
