package com.large.board.service.impl;

import com.large.board.common.code.Role;
import com.large.board.domain.repository.PostRepository;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostRequest;
import com.large.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Long register(Long userId, Role authority, PostRequest postRequest) {
        return null;
    }

    @Override
    public List<PostDTO> getMyPosts(Long userId) {
        return null;
    }

    @Override
    public void update(Long userId, Long postId, PostRequest postRequest) {

    }

    @Override
    public void delete(Long userId, Long postId) {

    }
}
