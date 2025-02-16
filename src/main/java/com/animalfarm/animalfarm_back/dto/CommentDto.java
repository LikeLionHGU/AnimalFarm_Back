package com.animalfarm.animalfarm_back.dto;

import com.animalfarm.animalfarm_back.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private String image;
    private LocalDateTime regDate;

    private String printDate;

    public static CommentDto from(Comment comment, String commentUrl)
    {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .image(commentUrl)
                .regDate(comment.getRegDate())
                .build();
    }
}


