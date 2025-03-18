package com.todo.controller;

import com.todo.entity.User;
import com.todo.repository.UserRepository;
import com.todo.service.UserService;
import com.todo.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService service;

    public UserController(JwtUtil jwtUtil, UserService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addNewUser(@Valid  @RequestBody User user){
        var savedUser = service.add(user);
        if(savedUser == null)
            return new ResponseEntity<>("Email already exits",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(savedUser,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (service.authenticate(user.getEmail(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
        }
    }
}
