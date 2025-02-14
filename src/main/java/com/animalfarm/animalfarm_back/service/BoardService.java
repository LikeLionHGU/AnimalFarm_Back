package com.animalfarm.animalfarm_back.service;

import com.amazonaws.services.s3.AmazonS3;

import com.animalfarm.animalfarm_back.controller.request.BoardCategoryRequest;
import com.animalfarm.animalfarm_back.controller.request.BoardSearchRequest;
import com.animalfarm.animalfarm_back.domain.Board;

import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final AmazonS3 amazonS3;
    private final S3UploadService s3UploadService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public BoardDto saveBoard(BoardDto boardDto, MultipartFile image, String dirName, User newUser) throws IOException {
        File uploadFile = s3UploadService.convert(image)
                .orElseThrow(() -> new IOException("MultipartFile -> File 변환 실패"));
        String boardImageUrl = s3UploadService.upload(uploadFile, dirName);
        Board board = Board.from(boardDto, boardImageUrl, newUser);
        boardRepository.save(board);
        return BoardDto.from(board, generateImageUrl(board.getImage()));
    }

    private String generateImageUrl(String storedFileName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + storedFileName;
    }


    public List<BoardDto> getMainFoundBoards() {
        List<Board> boards = boardRepository.findTop4ByBoardTypeOrderByRegDateDesc(0);
        return timeTypeBoards(boards);
    }

    public List<BoardDto> getAllCategoryFoundBoardNew(BoardCategoryRequest boardCategoryRequest) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateDesc(0, boardCategoryRequest.getCategory());
        List<BoardDto> boardDtos = timeTypeBoards(boards);
        return boardDtos;
    }

    public List<BoardDto> getAllCategoryFoundBoardOld(BoardCategoryRequest boardCategoryRequest) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateAsc(0, boardCategoryRequest.getCategory());
        List<BoardDto> boardDtos = timeTypeBoards(boards);
        return boardDtos;
    }

    public List<BoardDto> getAllCategoryFoundBoardSearch(BoardSearchRequest boardSearchRequest) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleOrderByRegDateDesc(0, boardSearchRequest.getCategory(), boardSearchRequest.getSearch());
        List<BoardDto> boardDtos = timeTypeBoards(boards);
        return boardDtos;
    }

    public List<BoardDto> timeTypeBoards(List<Board> boards) {
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
}

