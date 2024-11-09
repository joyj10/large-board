package com.large.board.service.impl;

import com.large.board.converter.PostConverter;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.repository.PostRepository;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.dto.response.PageResponse;
import com.large.board.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchServiceImpl implements PostSearchService {

    private final PostRepository postRepository;

    @Cacheable(
            value = "searchPosts",
            key = "'searchPosts:' + T(org.springframework.util.DigestUtils).md5DigestAsHex((#postSearchRequest.getTitleKeyword() + #postSearchRequest.getContentKeyword() +#postSearchRequest.getCategoryId() + #postSearchRequest.getUserId() +#postSearchRequest.getSortStatus() +#postSearchRequest.getPage() + #postSearchRequest.getSize()).getBytes())"
    )
    @Override
    public PageResponse<PostDTO> searchPosts(PostSearchRequest postSearchRequest) {
        Page<PostEntity> postEntities = postRepository.searchPosts(postSearchRequest);
        Page<PostDTO> dtos = PostConverter.toDto(postEntities);
        return PageResponse.convert(dtos);
    }
}
