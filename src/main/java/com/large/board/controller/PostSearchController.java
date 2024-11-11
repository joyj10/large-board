package com.large.board.controller;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.dto.response.CommonResponse;
import com.large.board.dto.response.PageResponse;
import com.large.board.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/search")
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;

    @PostMapping
    public CommonResponse<PageResponse<PostDTO>> search(@RequestBody PostSearchRequest postSearchRequest) {
        PageResponse<PostDTO> pageResponse = postSearchService.searchPosts(postSearchRequest);
        return CommonResponse.ok(pageResponse);
    }
}
