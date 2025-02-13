package com.animalfarm.animalfarm_back.service;

import com.animalfarm.animalfarm_back.domain.User;
import com.animalfarm.animalfarm_back.dto.UserDto;
import com.animalfarm.animalfarm_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User saveOrUpdateUser(String userId, String email, String name, String imageUrl) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setEmail(email);
            user.setName(name);
            user.setImage(imageUrl);
            return userRepository.save(user);
        } else {
            User newUser = new User();
            newUser.setId(userId);
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setImage(imageUrl);
            return userRepository.save(newUser);
        }
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }


}

