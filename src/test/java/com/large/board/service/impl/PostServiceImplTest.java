package com.large.board.service.impl;

import com.large.board.common.exception.EmptyDataException;
import com.large.board.domain.entity.CategoryEntity;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.CategoryRepository;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostRequest;
import com.large.board.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceImplTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    private UserEntity userEntity;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        userEntity = userRepository.save(UserEntity.create("testUser", "password", "nickname"));
        categoryEntity = categoryRepository.save(CategoryEntity.create("Test Category"));
    }

    @Test
    @DisplayName("게시글 등록 성공 테스트")
    void register() {
        // given
        PostRequest postRequest = new PostRequest("Test Title", "Test Contents", categoryEntity.getId(), null);

        // when
        Long postId = postService.register(userEntity.getId(), false, postRequest);

        // then
        PostEntity savedPost = postRepository.findById(postId).orElseThrow();
        assertEquals("Test Title", savedPost.getTitle());
        assertEquals(userEntity.getId(), savedPost.getUserEntity().getId());
    }

    @Test
    @DisplayName("나의 게시글 조회 성공 테스트")
    void getMyPosts() {
        // given
        PostEntity postEntity = postRepository.save(PostEntity.create("Test Title", "Test Contents", false, categoryEntity, userEntity, null));
        postRepository.save(postEntity);

        // when
        List<PostDTO> myPosts = postService.getMyPosts(userEntity.getId());

        // then
        assertEquals(1, myPosts.size());
        assertEquals(postEntity.getTitle(), myPosts.get(0).getTitle());
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    void update() {
        // given
        PostEntity post = postRepository.save(PostEntity.create("Old Title", "Old Contents", false, categoryEntity, userEntity, null));
        CategoryEntity categoryEntity2 = categoryRepository.save(CategoryEntity.create("Test Category2"));
        PostRequest postRequest = new PostRequest("Updated Title", "Updated Contents", categoryEntity2.getId(), null);

        // when
        postService.update(userEntity.getId(), post.getId(), postRequest);

        // then
        PostEntity updatedPost = postRepository.findById(post.getId()).orElseThrow();
        assertEquals(postRequest.getTitle(), updatedPost.getTitle());
        assertEquals(postRequest.getContents(), updatedPost.getContents());
        assertEquals(postRequest.getCategoryId(), updatedPost.getCategoryEntity().getId());
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 - 게시글 없을 경우 예외 발생")
    void update_NotFound() {
        // given
        Long nonExistentPostId = 999L;
        Long userId = userEntity.getId();
        PostRequest postRequest = new PostRequest("Title", "Contents", categoryEntity.getId(), null);


        // when & then
        try {
            postService.update(userId, nonExistentPostId, postRequest);
            fail("Expected an EmptyDataException to be thrown");
        } catch (EmptyDataException e) {
            assertEquals("수정 요청하신 게시글이 없습니다.", e.getMessage());
        }
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    void delete() {
        // given
        PostEntity post = postRepository.save(PostEntity.create("Title", "Contents", false, categoryEntity, userEntity, null));

        // when
        postService.delete(userEntity.getId(), post.getId());

        // then
        assertFalse(postRepository.existsById(post.getId()));
    }

    @Test
    @DisplayName("게시글 삭제 실패 테스트 - 게시글 없을 경우 예외 발생")
    void delete_NotFound() {
        // given
        Long nonExistentPostId = 999L;
        Long userId = userEntity.getId();

        // when & then
        try {
            postService.delete(userId, nonExistentPostId);
            fail("Expected an EmptyDataException to be thrown");
        } catch (EmptyDataException e) {
            assertEquals("삭제 요청하신 게시글이 없습니다.", e.getMessage());
        }
    }
}
