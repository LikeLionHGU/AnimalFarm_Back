package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.Comment;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {

    public static List<BoardDto> timeTypeBoards(List<Board> boards) {
        List<BoardDto> boardDtoList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Board board : boards) {
            LocalDateTime regDate = board.getRegDate();
            Duration diff = Duration.between(regDate, now);

            String printDate = calculateTimeTypeAndPrintDate(regDate, now, diff);

            int timeType = getTimeTypeBasedOnPrintDate(printDate);

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

    public static BoardDto timeTypeBoard(Board board, int type) {
        LocalDateTime regDate = board.getRegDate();
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(regDate, now);

        String printDate = calculateTimeTypeAndPrintDate(regDate, now, diff);

        BoardDto boardDto;
        if (type == 0) {
            boardDto = BoardDto.fromDetailTimeTypeAdded(board, getTimeTypeBasedOnPrintDate(printDate), printDate);
        } else {
            boardDto = BoardDto.fromDetailTimeTypeAddedLost(board, printDate);
            List<CommentDto> comments = boardDto.getComments();
            for (CommentDto comment : comments) {
                Duration diffComment = Duration.between(comment.getRegDate(), now);
                comment.setPrintDate(calculateTimeTypeAndPrintDate(comment.getRegDate(), now, diffComment));
            }
            boardDto.setComments(comments);

        }

        return boardDto;
    }

    private static String calculateTimeTypeAndPrintDate(LocalDateTime regDate, LocalDateTime now, Duration diff) {
        String printDate;

        if (now.toLocalDate().isEqual(regDate.toLocalDate())) {
            long minutesDiff = diff.toMinutes();
            if (minutesDiff < 1) {
                printDate = "방금 전";
            } else if (minutesDiff < 60) {
                printDate = minutesDiff + "분 전";
            } else {
                minutesDiff /= 60;
                printDate = minutesDiff + "시간 전";
            }
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
            printDate = regDate.toLocalDate().format(formatter);
        }

        return printDate;
    }

    private static int getTimeTypeBasedOnPrintDate(String printDate) {
        if (printDate.equals("방금 전")) {
            return 1;
        } else if (printDate.contains("분 전")) {
            return 2;
        } else if (printDate.contains("시간 전")) {
            return 3;
        } else {
            return 4;
        }
    }
}
