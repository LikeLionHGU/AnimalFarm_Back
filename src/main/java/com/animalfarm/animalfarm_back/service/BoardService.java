package com.animalfarm.animalfarm_back.service;

import com.amazonaws.services.s3.AmazonS3;

import com.animalfarm.animalfarm_back.controller.request.board.BoardCategoryRequest;
import com.animalfarm.animalfarm_back.controller.request.board.BoardSearchRequest;
import com.animalfarm.animalfarm_back.domain.Board;

import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.animalfarm.animalfarm_back.service.TimeService.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
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


    public List<BoardDto> getMainBoards(int boardType) {
        List<Board> boards = boardRepository.findTop4ByBoardTypeOrderByRegDateDesc(boardType);
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardNew(BoardCategoryRequest boardCategoryRequest, int boardType) {
        int category = boardCategoryRequest.getCategory();
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeOrderByRegDateDesc(boardType);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateDesc(boardType, boardCategoryRequest.getCategory());
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardOld(BoardCategoryRequest boardCategoryRequest, int boardType) {
        int category = boardCategoryRequest.getCategory();
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeOrderByRegDateAsc(boardType);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateAsc(boardType, boardCategoryRequest.getCategory());
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardSearchNew(BoardSearchRequest boardSearchRequest, int boardType) {
        int category = boardSearchRequest.getCategory();
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndTitleContainingOrderByRegDateDesc(boardType, boardSearchRequest.getSearch());
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleContainingOrderByRegDateDesc(boardType, category, boardSearchRequest.getSearch());
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardSearchOld(BoardSearchRequest boardSearchRequest, int boardType) {
        int category = boardSearchRequest.getCategory();
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndTitleContainingOrderByRegDateAsc(boardType, boardSearchRequest.getSearch());
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleContainingOrderByRegDateAsc(boardType, category, boardSearchRequest.getSearch());
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getMyPageMainBoard(User user, int boardType) {
        List<Board> boards = boardRepository.findTop2ByBoardTypeAndUserOrderByRegDateDesc(boardType, user);
        return timeTypeBoards(boards);
    }

    public List<BoardDto> getAllMyPageBoard(User user, int boardType) {
        List<Board> boards = boardRepository.findAllByBoardTypeAndUserOrderByRegDateDesc(boardType, user);
        return timeTypeBoards(boards);
    }

    public BoardDto getDetailFoundBoard(Long board_id) {
        Board boardEntity = null;
        Optional<Board> board = boardRepository.findById(board_id);
        if (board.isPresent()) {
            boardEntity = board.get();
        } else {
            return null;
        }
        return timeTypeBoard(boardEntity);
    }

    public String deleteById(Long board_id) {
        boardRepository.deleteById(board_id);
        return "Success";
    }

    @Transactional
    public int updateNewBoard(BoardDto boardDto, MultipartFile image, String dirName, Long board_id, String imageUrl) throws IOException {
        Board board = null;
        Optional<Board> boardOptional = boardRepository.findById(board_id);

        if (boardOptional.isPresent()) {
            board = boardOptional.get();
        } else {
            return 0;
        }

        String boardImageUrl = imageUrl; // 있으면 원래 값 없으면 null

        if (image != null) {
            File uploadFile = s3UploadService.convert(image)
                    .orElseThrow(() -> new IOException("MultipartFile -> File 변환 실패"));
            boardImageUrl = s3UploadService.upload(uploadFile, dirName);
        }

        board.update(boardDto, boardImageUrl);
        return 1;
    }

}


