package com.large.board.controller;

import com.large.board.dto.request.CategoryRequest;
import com.large.board.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Long> register(@Valid @RequestBody CategoryRequest categoryRequest) {
        Long categoryId = categoryService.register(categoryRequest);
        return ResponseEntity.ok(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public void update(@PathVariable(name = "categoryId") Long categoryId,
                       @Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.update(categoryId, categoryRequest);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable(name = "categoryId") Long categoryId) {
        categoryService.delete(categoryId);
    }
}
