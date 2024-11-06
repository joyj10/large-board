package com.large.board.service;

import com.large.board.common.code.Role;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostRequest;

import java.util.List;

public interface PostService {
    Long register(Long userId, Role authority, PostRequest postRequest);

    List<PostDTO> getMyPosts(Long userId);

    void update(Long userId, Long postId, PostRequest postRequest);

    void delete(Long userId, Long postId);
}
