package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.controller.request.board.BoardCategoryRequest;
import com.animalfarm.animalfarm_back.controller.request.board.BoardSearchRequest;
import com.animalfarm.animalfarm_back.domain.Board;

import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.repository.BoardRepository;
//import com.animalfarm.animalfarm_back.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.animalfarm.animalfarm_back.service.TimeService.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final S3UploadService s3UploadService;
//    private final NotificationRepository notificationRepository;

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

    public List<BoardDto> getAllCategoryBoardNew(int category, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeOrderByRegDateDesc(boardType);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateDesc(boardType, category);
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardOld(int category, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeOrderByRegDateAsc(boardType);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryOrderByRegDateAsc(boardType, category);
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardSearchNew(int category, String search, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndTitleContainingOrderByRegDateDesc(boardType, search);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleContainingOrderByRegDateDesc(boardType, category, search);
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardSearchOld(int category, String search, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndTitleContainingOrderByRegDateAsc(boardType, search);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleContainingOrderByRegDateAsc(boardType, category, search);
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getMyPageMainBoard(User user, int boardType) {

        List<Board> boards = new ArrayList<>();
        if (boardType == 1) {
            boards = boardRepository.findTop4ByUserAndBoardTypeOrderByRegDateDesc(user, boardType);
        } else {
            boards = boardRepository.findTop4ByUserAndBoardTypeOrderByRegDateDesc(user, boardType);
        }

        if (boards.isEmpty())
            return null;

        List<Board> boardList = timeLimitBoards(boards);

        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllMyPageBoardNew(User user, int boardType) {
        List<Board> boards;

        boards = boardRepository.findByUserAndBoardTypeOrderByRegDateDesc(user, boardType);
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllMyPageBoardOld(User user, int boardType) {
        List<Board> boards;

        boards = boardRepository.findByUserAndBoardTypeOrderByRegDateAsc(user, boardType);
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public BoardDto getDetailFoundBoard(Long board_id) {
        Board boardEntity;
        Optional<Board> board = boardRepository.findById(board_id);
        if (board.isPresent()) {
            boardEntity = board.get();
        } else {
            return null;
        }
        return timeTypeBoard(boardEntity, 0);
    }


    public BoardDto getDetailLostBoard(Long board_id, User presentUser) {
        Board boardEntity;

        Optional<Board> board = boardRepository.findById(board_id);
        if (board.isPresent()) {
            boardEntity = board.get();
        } else {
            return null;
        }

        return timeTypeBoard(boardEntity, 1);
    }

    public int deleteById(Long board_id) {
        try {
            boardRepository.deleteById(board_id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
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

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }

    public Board findById(Long board_id) {
        return boardRepository.findById(board_id).orElse(null);
    }

    @Transactional
    public int updateIsFound(BoardDto boardDto, Long board_id) {
        Board board = null;
        Optional<Board> boardOptional = boardRepository.findById(board_id);

        if (boardOptional.isPresent()) {
            board = boardOptional.get();
        } else {
            return 0;
        }

        board.updateIsFound(boardDto);
        return 1;
    }

}


