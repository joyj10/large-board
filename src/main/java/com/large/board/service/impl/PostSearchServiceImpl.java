package com.large.board.service.impl;

import com.large.board.converter.PostConverter;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.repository.PostRepository;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.dto.response.PageResponse;
import com.large.board.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostSearchServiceImpl implements PostSearchService {

    private final PostRepository postRepository;

    @Cacheable(
            value = "searchPosts",
            key = "'searchPosts:' + T(org.springframework.util.DigestUtils).md5DigestAsHex((#request.titleKeyword +#request.categoryId +#request.sortStatus +#request.page +#request.size).getBytes())"
            )
    @Override
    public Page<PostDTO> searchPosts(PostSearchRequest request) {
        log.debug("no cache");
        Page<PostEntity> postEntities = postRepository.searchPosts(request);
        return PostConverter.toDto(postEntities);
    }
}
