package com.large.board.service.impl;

import com.large.board.common.exception.EmptyDataException;
import com.large.board.domain.entity.CategoryEntity;
import com.large.board.domain.entity.CommentEntity;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.CategoryRepository;
import com.large.board.domain.repository.CommentRepository;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.request.CommentRequest;
import com.large.board.dto.request.CommentUpdateRequest;
import com.large.board.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    private UserEntity userEntity;
    private CategoryEntity categoryEntity;
    private PostEntity postEntity;

    @BeforeEach
    void setUp() {
        userEntity = userRepository.save(UserEntity.create("testUser", "password", "nickname"));
        categoryEntity = categoryRepository.save(CategoryEntity.create("Test Category"));
        postEntity = postRepository.save(PostEntity.create("Test Title", "Test Content", false, categoryEntity, userEntity, null));
    }

    @Test
    @DisplayName("댓글 등록 성공 테스트")
    void registerComment_Success() {
        // given
        CommentRequest request = new CommentRequest("Test Comment", postEntity.getId(), null);

        // when
        Long commentId = commentService.register(userEntity.getId(), request);

        // then
        CommentEntity savedComment = commentRepository.findById(commentId).orElse(null);

        assertNotNull(savedComment);
        assertEquals("Test Comment", savedComment.getContents());
        assertEquals(postEntity.getId(), savedComment.getPostEntity().getId());
        assertEquals(userEntity.getId(), savedComment.getUserEntity().getId());
    }

    @Test
    @DisplayName("댓글 등록 실패 테스트 - 존재 하지 않는 사용자")
    void registerComment_Fail_UserNotFound() {
        // given
        CommentRequest request = new CommentRequest("Test Comment", postEntity.getId(), null);

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> {
            commentService.register(999L, request);
        });
    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    void updateComment_Success() {
        // given
        CommentEntity comment = CommentEntity.create("Original Comment", postEntity, userEntity, null);
        commentRepository.save(comment);

        CommentUpdateRequest updateRequest = new CommentUpdateRequest("Updated Comment");

        // when
        commentService.update(userEntity.getId(), comment.getId(), updateRequest);

        // then
        CommentEntity updatedComment = commentRepository.findById(comment.getId()).orElse(null);

        assertNotNull(updatedComment);
        assertEquals(updateRequest.getContents(), updatedComment.getContents());
    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 - 다른 사용자의 댓글")
    void updateComment_Fail_NotOwner() {
        // given
        UserEntity anotherUser = userRepository.save(UserEntity.create("anotherUser", "password", "anotherNickname"));
        CommentEntity comment = CommentEntity.create("Original Comment", postEntity, anotherUser, null);
        commentRepository.save(comment);

        CommentUpdateRequest updateRequest = new CommentUpdateRequest("Updated Comment");

        // when & then
        Long userId = userEntity.getId();
        Long commentId = comment.getId();
        assertThrows(EmptyDataException.class, () -> {
            commentService.update(userId, commentId, updateRequest);
        });
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteComment_Success() {
        // given
        CommentEntity comment = CommentEntity.create("Comment to be deleted", postEntity, userEntity, null);
        commentRepository.save(comment);

        // when
        commentService.delete(userEntity.getId(), comment.getId());

        // then
        assertFalse(commentRepository.existsById(comment.getId()));
    }

    @Test
    @DisplayName("댓글 삭제 실패 테스트 - 존재하지 않는 댓글")
    void deleteComment_Fail_NotFound() {
        // when & then
        Long userId = userEntity.getId();
        assertThrows(EmptyDataException.class, () -> {
            commentService.delete(userId, 999L);  // 존재하지 않는 댓글 ID
        });
    }

    @Test
    @DisplayName("댓글 삭제 실패 테스트 - 다른 사용자의 댓글")
    void deleteComment_Fail_NotOwner() {
        // given
        UserEntity anotherUser = userRepository.save(UserEntity.create("anotherUser", "password", "anotherNickname"));
        CommentEntity comment = CommentEntity.create("Comment by another user", postEntity, anotherUser, null);
        commentRepository.save(comment);

        // when & then
        Long userId = userEntity.getId();
        Long commentId = comment.getId();
        assertThrows(EmptyDataException.class, () -> {
            commentService.delete(userId, commentId);
        });
    }
}
