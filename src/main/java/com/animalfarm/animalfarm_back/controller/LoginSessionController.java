package com.animalfarm.animalfarm_back.controller;

import com.animalfarm.animalfarm_back.controller.request.login.LoginTokenRequest;
import com.animalfarm.animalfarm_back.controller.response.login.LoginStatusResponse;
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
    public ResponseEntity<Map<String, String>> getClientId() {
        Map<String, String> response = new HashMap<>();
        response.put("clientId", clientId);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginStatusResponse> googleLogin(
            @RequestBody LoginTokenRequest request,
            HttpSession session) {
        String credential = request.getGoogleIdToken();
        LoginStatusResponse response = new LoginStatusResponse();
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(credential);

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
            session.setMaxInactiveInterval(-1); //session 무한 설정

            System.out.println(session.getAttribute("userId"));

            //session 저장 되는 것은 확인
            response.setIsLogin(1);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setIsLogin(0);
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