package com.large.board.dto;

import com.large.board.dto.response.TagDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String contents;
    private boolean isAdmin;
    private int views;
    private String categoryName;
    private String userName;
    private String userAccountId;
    private Long fileId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<TagDTO> tags;
}
