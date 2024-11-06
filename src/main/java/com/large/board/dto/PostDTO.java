package com.large.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private int id;
    private String name;
    private int isAdmin;
    private String contents;
    private int views;
    private int categoryId;
    private int userId;
    private int fileId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
