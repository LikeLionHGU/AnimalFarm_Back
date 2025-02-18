package com.animalfarm.animalfarm_back.domain;

import com.animalfarm.animalfarm_back.dto.SawPeopleDto;
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
public class SawPeople {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "sawpeople_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy/MM/dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(updatable = false)
    private LocalDateTime regDate;

    public static SawPeople from(Board newBoard, User newUser) {
        return SawPeople.builder()
                .board(newBoard)
                .user(newUser)
                .build();
    }
}
