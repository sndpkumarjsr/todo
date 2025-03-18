package com.todo.service;

import com.todo.entity.User;
import com.todo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    public User add(User user){
        User saved = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(saved == null)
            return userRepository.save(user);
        return null;
    }


}
