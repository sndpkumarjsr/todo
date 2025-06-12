package com.todo.service;

import com.todo.dto.UserDto;
import com.todo.entity.User;
import com.todo.repository.UserRepository;
import com.todo.util.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public boolean authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password);
        }
        return false;
    }

    public User add(UserDto dto){
        User saved = userRepository.findByEmail(dto.email()).orElse(null);
        if(saved == null) {
            User user = userMapper.getUser(dto);
            return userRepository.save(user);
        }
        return null;
    }
}
