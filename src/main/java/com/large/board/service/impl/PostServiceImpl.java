package com.large.board.service.impl;

import com.large.board.common.exception.EmptyDataException;
import com.large.board.converter.PostConverter;
import com.large.board.domain.entity.CategoryEntity;
import com.large.board.domain.entity.PostEntity;
import com.large.board.domain.entity.TagEntity;
import com.large.board.domain.entity.UserEntity;
import com.large.board.domain.repository.CategoryRepository;
import com.large.board.domain.repository.PostRepository;
import com.large.board.domain.repository.TagRepository;
import com.large.board.domain.repository.UserRepository;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostRequest;
import com.large.board.dto.request.TagRequest;
import com.large.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Long register(Long userId, Boolean isAdmin, PostRequest postRequest) {
        CategoryEntity categoryEntity = categoryRepository.getReferenceById(postRequest.getCategoryId());
        UserEntity userEntity = userRepository.getReferenceById(userId);

        PostEntity postEntity = PostEntity.create(
                postRequest.getTitle(), postRequest.getContents(), isAdmin,
                categoryEntity, userEntity, postRequest.getFileId()
        );

        // tag 추가
        for (TagRequest tagRequest : postRequest.getTags()) {
            TagEntity tagEntity = saveTagEntity(tagRequest);
            postEntity.addTag(tagEntity);
            tagEntity.addPostTag(postEntity);
        }

        return postRepository.save(postEntity).getId();
    }

    private TagEntity saveTagEntity(TagRequest tagRequest) {
        return tagRepository.save(TagEntity.create(tagRequest.getName(), tagRequest.getUrl()));
    }

    @Override
    public List<PostDTO> getMyPosts(Long userId) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        List<PostEntity> postEntities = postRepository.findAllByUserEntity(userEntity);
        return PostConverter.toDto(postEntities);
    }

    @Transactional
    @Override
    public void update(Long userId, Long postId, PostRequest postRequest) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        PostEntity postEntity = postRepository.findByIdAndUserEntity(postId, userEntity)
                .orElseThrow(() -> new EmptyDataException("수정 요청하신 게시글이 없습니다."));

        CategoryEntity categoryEntity = categoryRepository.getReferenceById(postRequest.getCategoryId());

        postEntity.update(postRequest.getTitle(), postRequest.getContents(), categoryEntity, postRequest.getFileId());

        // tag 변경 : 기존 tag 삭제 후 신규 추가
        postEntity.removeTags();
        for (TagRequest tagRequest : postRequest.getTags()) {
            TagEntity tagEntity = saveTagEntity(tagRequest);
            postEntity.addTag(tagEntity);
        }
    }

    @Transactional
    @Override
    public void delete(Long userId, Long postId) {
        UserEntity userEntity = userRepository.getReferenceById(userId);
        if (postRepository.countByIdAndUserEntity(postId, userEntity) == 0) {
            throw new EmptyDataException("삭제 요청하신 게시글이 없습니다.");
        }
        postRepository.deleteById(postId);
    }
}
