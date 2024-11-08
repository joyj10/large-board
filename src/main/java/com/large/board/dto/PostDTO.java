package com.large.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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

    @Override
    public String toString() {
        return "PostDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", isAdmin=" + isAdmin +
                ", views=" + views +
                ", categoryName='" + categoryName + '\'' +
                ", userName='" + userName + '\'' +
                ", userAccountId='" + userAccountId + '\'' +
                ", fileId=" + fileId +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
