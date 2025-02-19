package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.response.user.UserDetailResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public ResponseEntity<UserDetailResponse> getUserInfo(HttpSession session) {
        UserDetailResponse userDetailResponse = new UserDetailResponse();
        userDetailResponse.setIsLogin(loginOrNot(session));
        try {
            userDetailResponse.setUsername((String) session.getAttribute("name"));
            userDetailResponse.setEmail((String) session.getAttribute("email"));
            userDetailResponse.setImage((String) session.getAttribute("pictureUrl"));

            return ResponseEntity.ok(userDetailResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            userDetailResponse.setUsername("");
            userDetailResponse.setEmail("");
            userDetailResponse.setImage("");
            return ResponseEntity.ok(userDetailResponse);
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
