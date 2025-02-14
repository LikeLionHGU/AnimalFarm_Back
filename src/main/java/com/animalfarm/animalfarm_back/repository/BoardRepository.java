package com.animalfarm.animalfarm_back.repository;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findTop4ByBoardTypeOrderByRegDateDesc(int BoardType);

    List<Board> findAllByBoardTypeAndCategoryOrderByRegDateDesc(int BoardType, int category);

    List<Board> findAllByBoardTypeAndCategoryOrderByRegDateAsc(int BoardType, int category);

    List<Board> findAllByBoardTypeAndCategoryAndTitleOrderByRegDateDesc(int BoardType, int category, String search);

}

