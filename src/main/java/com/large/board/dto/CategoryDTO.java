package com.large.board.dto;

import com.large.board.common.code.SortStatus;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private int id;
    private String name;
    private SortStatus sortStatus;
    private int searchCount;    // 조회수
}
