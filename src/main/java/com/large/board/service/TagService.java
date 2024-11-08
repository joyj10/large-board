package com.large.board.service;

import com.large.board.dto.request.TagRequest;

public interface TagService {

    Long register(Long userId, TagRequest tagRequest);

    void update(Long tagId, TagRequest tagRequest);

    void delete(Long tagId);
}
