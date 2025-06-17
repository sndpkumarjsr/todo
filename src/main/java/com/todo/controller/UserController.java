package com.todo.controller;

import com.todo.dto.UserDto;
import com.todo.entity.User;
import com.todo.exception.UserException;
import com.todo.repository.UserRepository;
import com.todo.service.UserService;
import com.todo.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDto> addNewUser(@Valid  @RequestBody UserDto dto) throws UserException {
        logger.info("POST /users - Adding new user with email: {}", dto.email());
        UserDto createdUser = service.add(dto);
        logger.debug("User created: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@Valid @RequestBody UserDto dto) throws UserException {
        logger.info("POST /users/login - Attempting login for email: {}", dto.email());
        Map<String, String> authTokenMap = service.authenticate(dto.email(), dto.password());
        logger.debug("Login successful, token generated for email: {}", dto.email());
        return ResponseEntity.ok(authTokenMap);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto dto) throws UserException{
        logger.info("PUT /users - Updating user with email: {}", dto.email());
        UserDto updatedUser = service.update(dto);
        logger.debug("User updated: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }
}
