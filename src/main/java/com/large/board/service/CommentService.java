package com.large.board.service;

import com.large.board.dto.request.CommentRequest;
import com.large.board.dto.request.CommentUpdateRequest;

public interface CommentService {

    Long register(Long userId, CommentRequest commentRequest);

    void update(Long userId, Long commentId, CommentUpdateRequest updateRequest);

    void delete(Long userId, Long commentId);
}
