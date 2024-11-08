package com.large.board.service;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;

import java.util.List;

public interface PostSearchService {
    List<PostDTO> searchPosts(PostSearchRequest postSearchRequest);
}
