package com.large.board.controller;

import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.request.CommentRequest;
import com.large.board.dto.request.CommentUpdateRequest;
import com.large.board.dto.response.CommonResponse;
import com.large.board.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<Long> register(@AuthenticationPrincipal UserEntity userEntity,
                                         @Valid @RequestBody CommentRequest commentRequest) {
        Long commentId = commentService.register(userEntity.getId(), commentRequest);
        return CommonResponse.ok(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommonResponse<Long> update(@AuthenticationPrincipal UserEntity userEntity,
                                       @PathVariable(name = "commentId") Long commentId,
                                       @Valid @RequestBody CommentUpdateRequest updateRequest) {
        commentService.update(userEntity.getId(), commentId, updateRequest);
        return CommonResponse.ok(commentId);
    }

    @DeleteMapping("/{commentId}")
    public CommonResponse<Void> delete(@AuthenticationPrincipal UserEntity userEntity,
                                       @PathVariable(name = "commentId") Long commentId,
                                       @Valid @RequestBody CommentUpdateRequest updateRequest) {
        commentService.update(userEntity.getId(), commentId, updateRequest);
        return CommonResponse.ok(null);
    }
}
