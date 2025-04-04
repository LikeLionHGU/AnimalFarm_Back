package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.Notification;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.repository.BoardRepository;
import com.animalfarm.animalfarm_back.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public BoardDto saveBoard(BoardDto boardDto, MultipartFile image, String dirName, User newUser) throws IOException {
        String boardImageUrl;

        if (image.isEmpty()) {
            boardImageUrl = "https://hkwon.s3.ap-northeast-2.amazonaws.com/va/emptyImage.png";
        } else {
            File uploadFile = s3UploadService.convert(image)
                    .orElseThrow(() -> new IOException("MultipartFile -> File 변환 실패"));
            boardImageUrl = s3UploadService.upload(uploadFile, dirName);
        }

        Board board = Board.from(boardDto, boardImageUrl, newUser);
        boardRepository.save(board);
        return BoardDto.from(board, generateImageUrl(board.getImage()));
    }

    private String generateImageUrl(String storedFileName) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + storedFileName;
    }


    public List<BoardDto> getMainBoards(int boardType) {
        List<Board> boards = boardRepository.findTop4ByBoardTypeAndIsFoundOrderByRegDateDesc(boardType, 0);
        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardNew(int category, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndIsFoundOrderByRegDateDesc(boardType, 0);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndIsFoundOrderByRegDateDesc(boardType, category, 0);
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardOld(int category, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndIsFoundOrderByRegDateAsc(boardType, 0);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndIsFoundOrderByRegDateAsc(boardType, category, 0);
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardSearchNew(int category, String search, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndTitleContainingAndIsFoundOrderByRegDateDesc(boardType, search, 0);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleContainingAndIsFoundOrderByRegDateDesc(boardType, category, search, 0);
        }

        List<Board> boardList = timeLimitBoards(boards);
        return timeTypeBoards(boardList);
    }

    public List<BoardDto> getAllCategoryBoardSearchOld(int category, String search, int boardType) {
        List<Board> boards;
        if (category == 0) {
            boards = boardRepository.findAllByBoardTypeAndTitleContainingAndIsFoundOrderByRegDateAsc(boardType, search, 0);
        } else {
            boards = boardRepository.findAllByBoardTypeAndCategoryAndTitleContainingAndIsFoundOrderByRegDateAsc(boardType, category, search, 0);
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


    @Transactional
    public BoardDto getDetailLostBoard(Long board_id, User user) {
        Board boardEntity;

        Optional<Board> board = boardRepository.findById(board_id);
        if (board.isPresent()) {
            boardEntity = board.get();
        } else {
            return null;
        }
        if (!Objects.equals(user.getId(), "1")) {
            if (Objects.equals(user.getId(), boardEntity.getUser().getId())) {
                Optional<Notification> notificationOptional = notificationRepository.findByBoard(boardEntity);
                if (notificationOptional.isPresent()) {
                    Notification notification = notificationOptional.get();
                    notification.update2Read();
                }
            }

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
        try {
            Board board = null;
            Optional<Board> boardOptional = boardRepository.findById(board_id);

            if (boardOptional.isPresent()) {
                board = boardOptional.get();
            } else {
                return 0;
            }

            board.updateIsFound();
            return 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }

}


