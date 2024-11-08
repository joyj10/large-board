package com.large.board.dto.request;

import com.large.board.common.code.SortStatus;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchRequest {
    // 필터링 조건
    private String titleKeyword;
    private String contentKeyword;
    private Long categoryId;
    private Long userId;

    // 정렬 조건
    private SortStatus sortStatus;
}
