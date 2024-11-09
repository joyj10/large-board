package com.large.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.large.board.dto.request.CategoryRequest;
import com.large.board.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    @DisplayName("카테고리 등록 성공 테스트")
    void test_register() throws Exception {
        // given
        CategoryRequest categoryRequest = new CategoryRequest("테스트 카테고리");

        // when then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    @DisplayName("카테고리 등록 실패 테스트 - 유효성 검사 실패")
    void test_registerFailure_return400() throws Exception {
        // given
        CategoryRequest categoryRequest = new CategoryRequest(""); // 유효하지 않은 카테고리 이름

        // when then
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    @DisplayName("카테고리 수정 성공 테스트")
    void test_update() throws Exception {
        // given
        Long categoryId = categoryService.register(new CategoryRequest("테스트 카테고리"));
        CategoryRequest categoryRequest = new CategoryRequest("수정 카테고리");

        mockMvc.perform(patch("/categories/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    @DisplayName("카테고리 수정 실패 테스트 - 유효성 검사 실패")
    void test_updateFailure_return400() throws Exception {
        // given
        Long existingCategoryId = 1L; // 수정할 카테고리 ID
        CategoryRequest categoryRequest = new CategoryRequest("");

        mockMvc.perform(patch("/categories/{categoryId}", existingCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    @DisplayName("카테고리 삭제 성공 테스트")
    void test_delete() throws Exception {
        Long categoryId = categoryService.register(new CategoryRequest("테스트 카테고리"));

        mockMvc.perform(delete("/categories/{categoryId}", categoryId))
                .andExpect(status().isOk());
    }
}
