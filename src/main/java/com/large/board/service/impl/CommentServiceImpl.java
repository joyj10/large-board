package com.large.board.service.impl;

import com.large.board.common.exception.EmptyDataException;
import com.large.board.domain.entity.CommentEntity;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.CommentRepository;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.request.CommentRequest;
import com.large.board.dto.request.CommentUpdateRequest;
import com.large.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Long register(Long userId, CommentRequest commentRequest) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        PostEntity postEntity = postRepository.getReferenceById(commentRequest.getPostId());

        Long parentCommentId = commentRequest.getParentCommentId();
        CommentEntity parentCommentEntity = (parentCommentId != null && parentCommentId != 0) ? commentRepository.getReferenceById(parentCommentId) : null;

        CommentEntity commentEntity = CommentEntity.create(commentRequest.getContents(), postEntity, userEntity, parentCommentEntity);
        return commentRepository.save(commentEntity).getId();
    }

    @Transactional
    @Override
    public void update(Long userId, Long commentId, CommentUpdateRequest updateRequest) {
        CommentEntity commentEntity = getCommentEntity(userId, commentId);
        commentEntity.update(updateRequest.getContents());
    }

    @Transactional
    @Override
    public void delete(Long userId, Long commentId) {
        CommentEntity commentEntity = getCommentEntity(userId, commentId);
        commentRepository.delete(commentEntity);
    }

    private CommentEntity getCommentEntity(Long userId, Long commentId) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        return commentRepository.findByIdAndUserEntity(commentId, userEntity)
                .orElseThrow(() -> new EmptyDataException("해당 댓글이 없습니다."));
    }
}
