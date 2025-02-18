package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.Board;
import com.animalfarm.animalfarm_back.domain.Notification;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.BoardDto;
import com.animalfarm.animalfarm_back.dto.NotificationDto;
import com.animalfarm.animalfarm_back.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.animalfarm.animalfarm_back.service.TimeService.timeNotification;

@Service
@RequiredArgsConstructor
public class NotificationService {
    NotificationRepository notificationRepository;
    TimeService timeService;

    public List<NotificationDto> getAllNotifications(User user) {
        List<Notification> notifications = notificationRepository.findByUserAndIsRead(user, 0);
        return timeNotification(notifications);
    }
}
