package com.large.board.service;

import com.large.board.dto.request.CategoryRequest;

public interface CategoryService {
    Long register(CategoryRequest categoryRequest);

    void update(Long categoryId, CategoryRequest categoryRequest);

    void delete(Long categoryId);
}
