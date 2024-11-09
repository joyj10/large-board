package com.large.board.controller;

import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostRequest;
import com.large.board.dto.response.CommonResponse;
import com.large.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public CommonResponse<Long> register(@AuthenticationPrincipal UserEntity userEntity,
                                         @RequestBody PostRequest postRequest) {
        Long userId = userEntity.getId();
        Boolean isAdmin = userEntity.isAdmin();

        Long postId = postService.register(userId, isAdmin, postRequest);
        return CommonResponse.ok(postId);
    }

    @GetMapping("/my")
    public CommonResponse<List<PostDTO>> myPostInfo(@AuthenticationPrincipal UserEntity userEntity) {
        Long userId = userEntity.getId();
        List<PostDTO> postDTOList = postService.getMyPosts(userId);
        return CommonResponse.ok(postDTOList);
    }

    @PatchMapping("/{postId}")
    public CommonResponse<Long> update(@AuthenticationPrincipal UserEntity userEntity,
                                       @PathVariable(name = "postId") Long postId,
                                       @RequestBody PostRequest postRequest) {
        Long userId = userEntity.getId();
        postService.update(userId, postId, postRequest);
        return CommonResponse.ok(postId);
    }

    @DeleteMapping("/{postId}")
    public CommonResponse<Void> delete(@AuthenticationPrincipal UserEntity userEntity,
                                       @PathVariable(name = "postId") Long postId) {
        Long userId = userEntity.getId();
        postService.delete(userId, postId);
        return CommonResponse.ok(null);
    }
}
