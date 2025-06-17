package com.todo.service;

import com.todo.dto.UserDto;
import com.todo.entity.User;
import com.todo.exception.UserException;
import com.todo.repository.UserRepository;
import com.todo.util.JwtUtil;
import com.todo.util.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> authenticate(String email, String password) throws UserException {
        logger.info("Service: Authenticating user with email: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getPassword().equals(password)) {
                String token = jwtUtil.generateToken(email);
                logger.debug("Authentication successful for email: {}", email);

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return response;
            } else {
                logger.warn("Authentication failed: Invalid password for email: {}", email);
                throw new UserException("Invalid Credentials");
            }
        }

        logger.error("Authentication failed: User not found with email: {}", email);
        throw new UserException("User Not Found!!!");
    }

    public UserDto add(UserDto dto) throws UserException{
        logger.info("Service: Adding new user with email: {}", dto.email());

        Optional<User> existingUser = userRepository.findByEmail(dto.email());
        if (existingUser.isPresent()) {
            logger.warn("User creation failed: Email already exists - {}", dto.email());
            throw new UserException("Email already exists");
        }

        User user = userMapper.getUser(dto);
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(user.getEmail());

        User savedUser = userRepository.save(user);
        logger.debug("User successfully created with ID: {}", savedUser.getId());

        return userMapper.getUserDto(savedUser);
    }

    public UserDto update(UserDto dto) throws UserException{
        logger.info("Service: Updating user with email: {}", dto.email());

        Optional<User> userOpt = userRepository.findByEmail(dto.email());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            user.setPassword(dto.password());
            user.setModifiedAt(LocalDateTime.now());
            user.setModifiedBy(dto.email());

            User updatedUser = userRepository.save(user);
            logger.debug("User successfully updated with ID: {}", updatedUser.getId());

            return userMapper.getUserDto(updatedUser);
        }

        logger.error("User update failed: User not found with email: {}", dto.email());
        throw new UserException("User Not Found!!!");
    }
}
