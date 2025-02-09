package com.animalfarm.animalfarm_back.repository;

import com.animalfarm.animalfarm_back.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
