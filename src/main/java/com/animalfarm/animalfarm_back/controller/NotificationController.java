package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.response.notification.NotificationResponse;
import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.NotificationDto;
import com.animalfarm.animalfarm_back.service.NotificationService;
import com.animalfarm.animalfarm_back.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/notification")
    public ResponseEntity<NotificationResponse> getNotification(HttpSession session) {
        NotificationResponse notificationResponse = new NotificationResponse();
        try {
            notificationResponse.setIsLogin(loginOrNot(session));

            String userId = (String) session.getAttribute("userId");

            User sessionUser = userService.findUserById(userId);
            List<NotificationDto> notificationDtos = notificationService.getAllNotifications(sessionUser);

            if (notificationDtos.isEmpty()) {
                notificationResponse.setNotifications(null);
                return ResponseEntity.ok(notificationResponse);
            }
            notificationResponse.setNotifications(notificationDtos);
            return ResponseEntity.ok(notificationResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            notificationResponse.setNotifications(null);
            return ResponseEntity.ok(notificationResponse);
        }
    }

    private int loginOrNot(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return 0;
        }
        return 1;
    }
}
