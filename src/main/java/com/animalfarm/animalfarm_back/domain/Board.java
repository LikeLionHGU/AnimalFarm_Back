package com.animalfarm.animalfarm_back.domain;

import com.animalfarm.animalfarm_back.controller.request.BoardAddRequest;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    private String title;
    private String image;
    private String content;
    private String location;
    private double longitude;
    private double latitude;
    private String detailLocation;
    private int boardType;
    private String phoneNum;
    private int category;
    private int isFound;
    private int isRead;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    public static Board from(BoardAddRequest boardAddRequest, String imageURL) {
        return Board.builder()
                .title(boardAddRequest.getTitle())
                .image(imageURL)
                .content(boardAddRequest.getContent())
                .location(boardAddRequest.getLocation())
                .longitude(boardAddRequest.getLongitude())
                .latitude(boardAddRequest.getLatitude())
                .detailLocation(boardAddRequest.getDetailLocation())
                .boardType(boardAddRequest.getBoardType())
                .phoneNum(boardAddRequest.getPhoneNum())
                .category(boardAddRequest.getCategory())
                .isFound(0)
                .isRead(0)
                .build();
    }

    public static Board from(BoardDto boardDto, String imageURL) {
        return Board.builder()
                .title(boardDto.getTitle())
                .image(imageURL)
                .content(boardDto.getContent())
                .location(boardDto.getLocation())
                .latitude(boardDto.getLatitude())
                .longitude(boardDto.getLongitude())
                .detailLocation(boardDto.getDetailLocation())
                .boardType(boardDto.getBoardType())
                .phoneNum(boardDto.getPhoneNumber())
                .category(boardDto.getCategory())
                .isFound(boardDto.getIsFound())
                .isRead(boardDto.getIsRead())
                .regDate(boardDto.getRegDate())
                .updateDate(boardDto.getUpdateDate())
                .build();
    }
}
