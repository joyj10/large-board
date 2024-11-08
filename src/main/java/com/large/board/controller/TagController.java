package com.large.board.controller;

import com.large.board.domain.entity.UserEntity;
import com.large.board.dto.request.TagRequest;
import com.large.board.dto.response.CommonResponse;
import com.large.board.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<Long> register(@AuthenticationPrincipal UserEntity userEntity,
                                         @Valid @RequestBody TagRequest tagRequest) {
        Long tagId = tagService.register(userEntity.getId(), tagRequest);
        return CommonResponse.ok(tagId);
    }

    @PatchMapping("/{tagId}")
    public CommonResponse<Long> update(@PathVariable(name = "tagId") Long tagId,
                                       @Valid @RequestBody TagRequest tagRequest) {
        tagService.update(tagId, tagRequest);
        return CommonResponse.ok(tagId);
    }

    @DeleteMapping("/{tagId}")
    public CommonResponse<Void> delete(@PathVariable(name = "tagId") Long tagId) {
        tagService.delete(tagId);
        return CommonResponse.ok(null);
    }
}
