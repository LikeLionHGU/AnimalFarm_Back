package com.animalfarm.animalfarm_back.domain;

import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "board_id")
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

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<SawPeople> sawPeopleList = new ArrayList<>();

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy/MM/dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy/MM/dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    public static Board from(BoardDto boardDto, String imageURL, User user) {
        return Board.builder()
                .title(boardDto.getTitle())
                .image(imageURL)
                .content(boardDto.getContent())
                .location(boardDto.getLocation())
                .latitude(boardDto.getLatitude())
                .longitude(boardDto.getLongitude())
                .detailLocation(boardDto.getDetailLocation())
                .boardType(boardDto.getBoardType())
                .phoneNum(boardDto.getPhoneNum())
                .category(boardDto.getCategory())
                .user(user)
                .isFound(boardDto.getIsFound())
                .isRead(boardDto.getIsRead())
                .regDate(boardDto.getRegDate())
                .updateDate(boardDto.getUpdateDate())
                .build();
    }

    public void update(BoardDto boardDto, String imageURL) {
        this.title = boardDto.getTitle();
        this.image = imageURL;
        this.content = boardDto.getContent();
        this.location = boardDto.getLocation();
        this.longitude = boardDto.getLongitude();
        this.latitude = boardDto.getLatitude();
        this.detailLocation = boardDto.getDetailLocation();
        this.boardType = boardDto.getBoardType();
        this.phoneNum = boardDto.getPhoneNum();
        this.category = boardDto.getCategory();
        this.isFound = boardDto.getIsFound();
        this.isRead = boardDto.getIsRead();
    }

    public void updateIsFound() {
        this.isFound = 1;
    }

}
