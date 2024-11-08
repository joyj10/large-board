package com.large.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "내용은 필수 입니다.")
    private String contents;

    @NotNull(message = "게시글 아이디는 필수입니다.")
    private Long postId;

    private Long parentCommentId;
}
