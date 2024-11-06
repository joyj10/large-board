package com.large.board.controller;

import com.large.board.common.code.Role;
import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.PostDTO;
import com.large.board.dto.request.PostRequest;
import com.large.board.dto.response.CommonResponse;
import com.large.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResponse<Long>> register(@AuthenticationPrincipal UserEntity userEntity,
                                                         @RequestBody PostRequest postRequest) {
        Long userId = userEntity.getId();
        Role authority = userEntity.getAuthority();
        Long postId = postService.register(userId, authority, postRequest);
        return ResponseEntity.ok(CommonResponse.ok(postId));
    }

    @GetMapping("/my")
    public ResponseEntity<CommonResponse<List<PostDTO>>> myPostInfo(@AuthenticationPrincipal UserEntity userEntity) {
        Long userId = userEntity.getId();
        List<PostDTO> postDTOList = postService.getMyPosts(userId);
        return ResponseEntity.ok(CommonResponse.ok(postDTOList));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<CommonResponse<Long>> update(@AuthenticationPrincipal UserEntity userEntity,
                                                       @PathVariable(name = "postId") Long postId,
                                                       @RequestBody PostRequest postRequest) {
        Long userId = userEntity.getId();
        postService.update(userId, postId, postRequest);
        return ResponseEntity.ok(CommonResponse.ok(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse<Long>> delete(@AuthenticationPrincipal UserEntity userEntity,
                                                       @PathVariable(name = "postId") Long postId) {
        Long userId = userEntity.getId();
        postService.delete(userId, postId);
        return ResponseEntity.ok(CommonResponse.ok(postId));
    }
}
