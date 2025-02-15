package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    public static List<BoardDto> timeTypeBoards(List<Board> boards) {
        List<BoardDto> boardDtoList = new ArrayList<>();


        for (Board board : boards) {
            LocalDateTime regDate = board.getRegDate();
            LocalDateTime now = LocalDateTime.now();
            Duration diff = Duration.between(regDate, now);

            int timeType;
            String printDate;

            if (now.toLocalDate().isEqual(regDate.toLocalDate())) {
                long minutesDiff = diff.toMinutes();
                if (minutesDiff < 1) {
                    timeType = 1;
                    printDate = "방금 전";
                } else if (minutesDiff < 60) {
                    timeType = 2;
                    printDate = minutesDiff + "분 전";
                } else {
                    timeType = 3;
                    printDate = minutesDiff + "분 전";
                }
            } else {
                timeType = 4;
                printDate = regDate.toLocalDate().toString();
            }


            BoardDto boardDto = BoardDto.fromTimeTypeAdded(board, timeType, printDate);
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    public static List<Board> timeLimitBoards(List<Board> boards) {
        List<Board> newBoards = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Board board : boards) {
            Duration diff = Duration.between(board.getRegDate(), now);
            long daysDiff = diff.toDays();
            if (daysDiff <= 14) {
                newBoards.add(board);
            }
        }
        return newBoards;
    }

    public static BoardDto timeTypeBoard(Board board) {
        LocalDateTime regDate = board.getRegDate();
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(regDate, now);

        int timeType;
        String printDate;

        if (now.toLocalDate().isEqual(regDate.toLocalDate())) {
            long minutesDiff = diff.toMinutes();
            if (minutesDiff < 1) {
                timeType = 1;
                printDate = "방금 전";
            } else if (minutesDiff < 60) {
                timeType = 2;
                printDate = minutesDiff + "분 전";
            } else {
                timeType = 3;
                printDate = minutesDiff + "분 전";
            }
        } else {
            timeType = 4;
            printDate = regDate.toLocalDate().toString();
        }


        BoardDto boardDto = BoardDto.fromTimeTypeAdded(board, timeType, printDate);
        return boardDto;
    }
}
