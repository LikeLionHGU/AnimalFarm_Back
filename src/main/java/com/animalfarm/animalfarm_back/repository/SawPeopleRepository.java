package com.animalfarm.animalfarm_back.repository;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.SawPeople;
import com.animalfarm.animalfarm_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SawPeopleRepository extends JpaRepository<SawPeople, Long> {

    List<SawPeople> findAllByBoardAndUser(Board board, User user);

}
