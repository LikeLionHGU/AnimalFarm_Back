package com.animalfarm.animalfarm_back.repository;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findTop4ByBoardTypeOrderByRegDateDesc(int boardType);

    List<Board> findAllByBoardTypeAndCategoryOrderByRegDateDesc(int boardType, int category);

    List<Board> findAllByBoardTypeAndCategoryOrderByRegDateAsc(int boardType, int category);

    List<Board> findAllByBoardTypeAndCategoryAndTitleOrderByRegDateDesc(int boardType, int category, String search);

    List<Board> findTop2ByBoardTypeAndUserOrderByRegDateDesc(int boardType, User user);

    List<Board> findAllByBoardTypeAndUserOrderByRegDateDesc(int boardType, User user);

}

