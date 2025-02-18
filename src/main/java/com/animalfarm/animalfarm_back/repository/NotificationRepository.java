package com.animalfarm.animalfarm_back.repository;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.Comment;
import com.animalfarm.animalfarm_back.domain.Notification;
import com.animalfarm.animalfarm_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndBoard(User user, Board board);
}
