package com.large.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagRequest {
    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotNull(message = "ur는 필수입니다.")
    private String url;

    @NotNull(message = "게시글 아이디는 필수입니다.")
    private Long postId;
}
