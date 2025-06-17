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
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> authenticate(String email, String password) throws UserException {
        logger.info("Service :- authenticate user email : {} and password : {}",email,password);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
             if(user.getPassword().equals(password)){
                String token = jwtUtil.generateToken(email);
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return response;
            }else{
                 throw new UserException("Invalid Credentials");
             }
        }
        throw  new UserException("User Not Found!!!");
    }

    public UserDto add(UserDto dto) throws UserException{
        logger.info("Service : Add user : {}",dto);
        User saved = userRepository.findByEmail(dto.email()).orElse(null);
        if(saved == null) {
            User user = userMapper.getUser(dto);
            user.setCreatedAt(LocalDateTime.now());
            user.setCreatedBy(user.getEmail());
            User savedUser = userRepository.save(user);
            return userMapper.getUserDto(savedUser);
        }
        throw new UserException("Email already exits");
    }

    public UserDto update(UserDto dto) throws UserException{
        logger.info("Service : Update user : {}",dto);
        Optional<User> userOpt = userRepository.findByEmail(dto.email());
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setPassword(dto.password());
            user.setModifiedAt(LocalDateTime.now());
            user.setModifiedBy(dto.email());

            User updateUser = userRepository.save(user);
            return userMapper.getUserDto(updateUser);
        }
        throw new UserException("User Not Found!!!");
    }
}
