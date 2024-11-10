package com.large.board.service.impl;

import com.large.board.domain.entity.CategoryEntity;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.TagEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.CategoryRepository;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.TagRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.request.TagRequest;
import com.large.board.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TagServiceImplTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    private Long userId;
    private Long postId;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = userRepository.save(UserEntity.create("testUser", "password", "nickname"));
        CategoryEntity categoryEntity = categoryRepository.save(CategoryEntity.create("Test Category"));
        PostEntity postEntity = postRepository.save(PostEntity.create("Test Title", "Test Content", false, categoryEntity, userEntity, null));

        userId = userEntity.getId();
        postId = postEntity.getId();
    }

    @Test
    @DisplayName("태그 등록 성공 테스트")
    void registerTag_Success() {
        // given
        TagRequest tagRequest = new TagRequest("Test Tag", "test url", postId);

        // when
        Long tagId = tagService.register(userId, tagRequest);

        // then
        assertNotNull(tagId);

        TagEntity savedTag = tagRepository.findById(tagId).orElse(null);
        assertNotNull(savedTag);
        assertEquals(tagRequest.getName(), savedTag.getName());
        assertEquals(tagRequest.getUrl(), savedTag.getUrl());
    }

    @Test
    @DisplayName("태그 등록 실패 테스트 - 존재하지 않는 게시글")
    void registerTag_Fail_NoPost() {
        // given
        TagRequest tagRequest = new TagRequest("Invalid Tag", "test url", -1L);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> tagService.register(userId, tagRequest));
    }

    @Test
    @DisplayName("태그 수정 성공 테스트")
    void updateTag_Success() {
        // given
        TagRequest tagRequest = new TagRequest("Initial Tag", "test url", postId);
        Long tagId = tagService.register(userId, tagRequest);

        // when
        TagRequest updateRequest = new TagRequest("Updated Tag", "update url", postId);
        tagService.update(tagId, updateRequest);

        // then
        TagEntity updatedTag = tagRepository.findById(tagId).orElse(null);
        assertNotNull(updatedTag);
        assertEquals(updateRequest.getName(), updatedTag.getName());
        assertEquals(updateRequest.getUrl(), updatedTag.getUrl());
    }

    @Test
    @DisplayName("태그 수정 실패 테스트 - 존재하지 않는 태그")
    void updateTag_Fail_NoTag() {
        // given
        TagRequest updateRequest = new TagRequest("Non-Existent Tag", "test url", postId);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> tagService.update(-1L, updateRequest));
    }

    @Test
    @DisplayName("태그 삭제 성공 테스트")
    void deleteTag_Success() {
        // given
        TagRequest tagRequest = new TagRequest("Tag to Delete", "test url", postId);
        Long tagId = tagService.register(userId, tagRequest);

        // when
        tagService.delete(tagId);

        // then
        assertFalse(tagRepository.findById(tagId).isPresent());
    }

    @Test
    @DisplayName("태그 삭제 실패 테스트 - 존재하지 않는 태그")
    void deleteTag_Fail_NoTag() {
        // given, when & then
        assertThrows(EntityNotFoundException.class, () -> tagService.delete(-1L));
    }
}
