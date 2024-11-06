package com.large.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @NotBlank(message = "이름은 필수 입니다.")
    private String name;

    @NotBlank(message = "내용은 필수 입니다.")
    private String contents;

    @NotNull(message = "카테고리 정보는 필수입니다.")
    private int categoryId;

    private int fileId;
}
