package com.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponseMessage {
    private String message;
    private HttpStatus status;
    private HttpStatusCode code;
}
