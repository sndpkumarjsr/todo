package com.todo.exception;

import com.todo.entity.APIResponseMessage;
import jakarta.validation.UnexpectedTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControlException {

    private static final Logger logger = LoggerFactory.getLogger(ControlException.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        logger.warn("Validation error: {}", ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<String> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        logger.error("Unexpected validation type: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("Validation Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("Malformed request body: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("Validation Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<APIResponseMessage> handleUserException(UserException ex){
        HttpStatus status = ex.getMessage().equalsIgnoreCase("Invalid Credentials")
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.BAD_REQUEST;

        logger.error("UserException: {}", ex.getMessage(), ex);

        APIResponseMessage response = APIResponseMessage.builder()
                .message(ex.getMessage())
                .status(status)
                .code(HttpStatusCode.valueOf(status.value()))
                .build();

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<APIResponseMessage> handleTaskException(TaskException ex){
        logger.error("TaskException: {}", ex.getMessage(), ex);
        APIResponseMessage responseMessage = new APIResponseMessage().builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatusCode.valueOf(500))
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponseMessage> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);

        APIResponseMessage response = APIResponseMessage.builder()
                .message("Something went wrong. Please contact support.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatusCode.valueOf(500))
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
