package com.large.board.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.large.board.common.code.SortStatus;
import com.large.board.domain.entity.CategoryEntity;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.CategoryRepository;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.service.PostSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableCaching
class PostSearchServiceImplTest {

    @Autowired
    private PostSearchService postSearchService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CacheManager cacheManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserEntity userEntity;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setUp() {
        userEntity = userRepository.save(UserEntity.create("testUser", "password", "nickname"));
        categoryEntity = categoryRepository.save(CategoryEntity.create("Test Category"));

        // 테스트 데이터를 사전에 저장
        PostEntity post1 = PostEntity.create("Test Title 1", "Test Content 1", false, categoryEntity, userEntity, null);
        PostEntity post2 = PostEntity.create("Test Title 2", "Test Content 2", false, categoryEntity, userEntity, null);
        postRepository.save(post1);
        postRepository.save(post2);
    }

    @Test
    @DisplayName("게시글 검색 조회 성공 테스트 + 검색 결과 캐싱")
    void searchPosts_cacheable() {
        // given
        PostSearchRequest postSearchRequest = new PostSearchRequest();
        postSearchRequest.setTitleKeyword("Test title 2");
        postSearchRequest.setCategoryId(categoryEntity.getId());
        postSearchRequest.setSortStatus(SortStatus.NEWEST);

        // when
        postSearchService.searchPosts(postSearchRequest);

        // 캐시 확인 (캐시 키 사용)
        Cache.ValueWrapper cachedValue = cacheManager.getCache("searchPosts")
                .get("searchPosts:" + getCacheKey(postSearchRequest));

        // 캐시 유효성 검증
        assertThat(cachedValue).isNotNull();
    }

    private String getCacheKey(PostSearchRequest request) {
        return org.springframework.util.DigestUtils.md5DigestAsHex((
                        request.getTitleKeyword() +
                        request.getContentKeyword() +
                        request.getCategoryId() +
                        request.getUserId() +
                        request.getSortStatus()
                ).getBytes()
        );
    }
}
