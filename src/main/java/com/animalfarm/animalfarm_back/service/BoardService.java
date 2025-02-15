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
import java.util.Date;
import java.util.List;

import static com.animalfarm.animalfarm_back.service.TimeService.timeLimitBoards;
import static com.animalfarm.animalfarm_back.service.TimeService.timeTypeBoards;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final AmazonS3 amazonS3;
    private final S3UploadService s3UploadService;
    private final TimeService timeService;

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
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryFoundBoardNew(BoardCategoryRequest boardCategoryRequest) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateDesc(0, boardCategoryRequest.getCategory());
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryFoundBoardOld(BoardCategoryRequest boardCategoryRequest) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateAsc(0, boardCategoryRequest.getCategory());
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryFoundBoardSearch(BoardSearchRequest boardSearchRequest) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleOrderByRegDateDesc(0, boardSearchRequest.getCategory(), boardSearchRequest.getSearch());
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getMyPageMainBoard(User user) {
        List<Board> boards = boardRepository.findTop4ByBoardTypeAndUserOrderByRegDateDesc(0, user);
        List<BoardDto> boardDtoList = timeTypeBoards(boards);
        return boardDtoList;
    }

    public List<BoardDto> getAllMyPageBoard(User user) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndUserOrderByRegDateDesc(0, user);
        List<BoardDto> boardDtoList = timeTypeBoards(boards);
        return boardDtoList;
    }
}

