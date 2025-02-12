package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import jakarta.servlet.http.HttpSession;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginSessionController {

    private final UserService userService;

    @Value("${google.oauth.client-id}")
    private String clientId;

    public LoginSessionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login/clientid")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok(clientId);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> googleLogin(@RequestParam("googleIdToken") String credential, HttpSession session) {
        // ID Token 검증 및 사용자 정보 추출
        System.out.println(clientId);
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
        try {

            GoogleIdToken idToken = verifier.verify(credential);
            if (idToken != null) {
                Payload payload = idToken.getPayload();

                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");


                userService.saveOrUpdateUser(userId, email, name, pictureUrl);

                // 세션 객체에 사용자 정보 저장
                session.setAttribute("userId", userId);
                session.setAttribute("email", email);
                session.setAttribute("name", name);
                session.setAttribute("pictureUrl", pictureUrl);

                Map<String, Object> response = new HashMap<>();
                response.put("isLogin", 1);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("isLogin", 0);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("isLogin", 0);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");
        String name = (String) session.getAttribute("name");

        if (userId != null) {
            return ResponseEntity.ok(Map.of(
                    "id", userId,
                    "email", email,
                    "name", name
            ));
        } else {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "User not logged in"));
        }
    }


}