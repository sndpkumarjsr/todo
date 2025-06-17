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
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDto> addNewUser(@Valid  @RequestBody UserDto dto) throws UserException {
        logger.info("Controller : Add new User {}"+dto);
        return new ResponseEntity<>(service.add(dto),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@Valid @RequestBody UserDto dto) throws UserException {
        logger.info("Controller : Login by User : {}"+dto);
        return ResponseEntity.ok(service.authenticate(dto.email(), dto.password()));
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserDto dto) throws UserException{
        logger.info("Controller : User Update of {}"+dto);
        return new ResponseEntity<>(service.update(dto),HttpStatus.OK);
    }
}
