package com.animalfarm.animalfarm_back.repository;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findTop4ByBoardTypeOrderByRegDateDesc(int boardType);

    List<Board> findAllByBoardTypeAndCategoryOrderByRegDateDesc(int boardType, int category);

    List<Board> findAllByBoardTypeOrderByRegDateDesc(int boardType);

    List<Board> findAllByBoardTypeAndCategoryOrderByRegDateAsc(int boardType, int category);

    List<Board> findAllByBoardTypeOrderByRegDateAsc(int boardType);

    List<Board> findAllByBoardTypeAndCategoryAndTitleContainingOrderByRegDateDesc(int boardType, int category, String search);

    List<Board> findAllByBoardTypeAndTitleContainingOrderByRegDateDesc(int boardType, String search);

    List<Board> findAllByBoardTypeAndCategoryAndTitleContainingOrderByRegDateAsc(int boardType, int category, String search);

    List<Board> findAllByBoardTypeAndTitleContainingOrderByRegDateAsc(int boardType, String search);

    List<Board> findTop4ByUserIdAndBoardTypeOrderByRegDateDesc(String userId, int boardType);

    List<Board> findTop2ByUserIdAndBoardTypeOrderByRegDateDesc(String userId, int boardType);

    List<Board> findByUserIdAndBoardTypeOrderByRegDateDesc(String userId, int boardType);

    List<Board> findByUserIdAndBoardTypeOrderByRegDateAsc(String userId, int boardType);

    List<Board> findAllByBoardTypeAndUserOrderByRegDateDesc(int boardType, User user);

}

