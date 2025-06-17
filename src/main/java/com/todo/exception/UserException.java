package com.todo.exception;

import com.todo.entity.User;

import java.io.IOException;

public class UserException extends IOException {
    public UserException(String message){
        super(message);
    }
}
