package com.large.board.controller;

import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostSearchRequest;
import com.large.board.dto.response.CommonResponse;
import com.large.board.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts/search")
@Slf4j
@RequiredArgsConstructor
public class PostSearchController {

    private final PostSearchService postSearchService;

    @PostMapping
    public ResponseEntity<CommonResponse<List<PostDTO>>> search(@RequestBody PostSearchRequest postSearchRequest) {
        List<PostDTO> postDTOList = postSearchService.searchPosts(postSearchRequest);
        return ResponseEntity.ok(CommonResponse.ok(postDTOList));
    }
}
