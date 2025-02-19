package com.animalfarm.animalfarm_back.repository;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findTop4ByBoardTypeAndIsFoundOrderByRegDateDesc(int boardType, int isFound);

    List<Board> findAllByBoardTypeAndCategoryAndIsFoundOrderByRegDateDesc(int boardType, int category, int isFound);

    List<Board> findAllByBoardTypeAndIsFoundOrderByRegDateDesc(int boardType, int isFound);

    List<Board> findAllByBoardTypeAndCategoryAndIsFoundOrderByRegDateAsc(int boardType, int category, int isFound);

    List<Board> findAllByBoardTypeAndIsFoundOrderByRegDateAsc(int boardType, int isFound);

    List<Board> findAllByBoardTypeAndCategoryAndTitleContainingAndIsFoundOrderByRegDateDesc(int boardType, int category, String search, int isFound);

    List<Board> findAllByBoardTypeAndTitleContainingAndIsFoundOrderByRegDateDesc(int boardType, String search, int isFound);

    List<Board> findAllByBoardTypeAndCategoryAndTitleContainingAndIsFoundOrderByRegDateAsc(int boardType, int category, String search, int isFound);

    List<Board> findAllByBoardTypeAndTitleContainingAndIsFoundOrderByRegDateAsc(int boardType, String search, int isFound);

    List<Board> findTop4ByUserAndBoardTypeOrderByRegDateDesc(User user, int boardType);

    List<Board> findByUserAndBoardTypeOrderByRegDateDesc(User user, int boardType);

    List<Board> findByUserAndBoardTypeOrderByRegDateAsc(User user, int boardType);
}

