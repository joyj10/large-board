package com.large.board.service.impl;

import com.large.board.common.exception.EmptyDataException;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.TagEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.TagRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.request.TagRequest;
import com.large.board.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Override
    public Long register(Long userId, TagRequest tagRequest) {
        UserEntity userEntity = userRepository.getReferenceById(userId);

        PostEntity postEntity = postRepository.findByIdAndUserEntity(tagRequest.getPostId(), userEntity)
                .orElseThrow(() -> new EmptyDataException("해당 게시글이 없습니다."));

        TagEntity tagEntity = TagEntity.create(tagRequest.getName(), tagRequest.getUrl());
        tagEntity.addPostTag(postEntity);

        return tagRepository.save(tagEntity).getId();
    }

    @Override
    public void update(Long tagId, TagRequest tagRequest) {
        TagEntity tagEntity = tagRepository.findById(tagId)
                .orElseThrow(() -> new EmptyDataException("해당 태그가 없습니다."));
        tagEntity.update(tagRequest.getName(), tagRequest.getUrl());
    }

    @Override
    public void delete(Long tagId) {
        TagEntity tagEntity = tagRepository.findById(tagId)
                .orElseThrow(() -> new EmptyDataException("해당 태그가 없습니다."));
        tagRepository.delete(tagEntity);
    }
}
