package com.animalfarm.animalfarm_back.domain;

import com.animalfarm.animalfarm_back.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "comment_id")
    private Long id;

    private String content;
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn (name = "board_id" ,nullable = false)
    private Board board;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy/MM/dd HH:mm", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate;

    public static Comment from(CommentDto commentDto, String commentUrl, User user, Board board) {
        return Comment.builder()
                .content(commentDto.getContent())
                .image(commentUrl)
                .user(user)
                .board(board)
                .build();
    }
    public static Comment fromWithoutImage(CommentDto commentDto, User user, Board board) {
        return Comment.builder()
                .content(commentDto.getContent())
                .user(user)
                .board(board)
                .build();
    }
}
