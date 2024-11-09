package com.large.board.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
    private String url;
}
