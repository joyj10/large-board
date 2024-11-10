package com.large.board.service.impl;

import com.large.board.domain.entity.CommentEntity;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.CommentRepository;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.request.CommentRequest;
import com.large.board.dto.request.CommentUpdateRequest;
import com.large.board.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
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
        PostEntity postEntity = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));

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
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글이 없습니다."));
    }
}
