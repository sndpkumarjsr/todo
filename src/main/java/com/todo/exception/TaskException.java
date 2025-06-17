package com.todo.exception;

import java.io.IOException;

public class TaskException extends IOException {
    public TaskException(String message){
        super(message);
    }
}
