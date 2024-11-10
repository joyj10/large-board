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
    private Long categoryId;

    // 페이지 조회 조건
    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 50;

    // 정렬 조건
    private SortStatus sortStatus;
}
