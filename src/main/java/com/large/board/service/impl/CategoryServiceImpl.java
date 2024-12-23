package com.large.board.service.impl;

import com.large.board.domain.entity.CategoryEntity;
import com.large.board.domain.repository.CategoryRepository;
import com.large.board.dto.request.CategoryRequest;
import com.large.board.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Long register(CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = CategoryEntity.create(categoryRequest.getName());
        return categoryRepository.save(categoryEntity).getId();
    }

    @Transactional
    @Override
    public void update(Long categoryId, CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = getCategoryEntity(categoryId);
        categoryEntity.update(categoryRequest.getName());
    }

    @Transactional
    @Override
    public void delete(Long categoryId) {
        CategoryEntity categoryEntity = getCategoryEntity(categoryId);
        categoryEntity.delete();
    }

    private CategoryEntity getCategoryEntity(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리 데이터가 없습니다."));
    }
}
