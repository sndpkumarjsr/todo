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

    private static Logger logger = LoggerFactory.getLogger(ControlException.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp){
        Map<String,String> errors = new HashMap<>();
        exp.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(UnexpectedTypeException.class)
//    public ResponseEntity<String> handleUnexpectedTypeException(UnexpectedTypeException ex) {
//        return new ResponseEntity<>("Validation Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Validation Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<APIResponseMessage> handleUserException(UserException exp){
        logger.error("Exception : Error in User : {}",exp.getMessage(),UserException.class);
        if(exp.getMessage().equals("Invalid Credentials")){
            APIResponseMessage responseMessage = new APIResponseMessage().builder()
                    .message(exp.getMessage())
                    .status(HttpStatus.UNAUTHORIZED)
                    .code(HttpStatusCode.valueOf(401))
                    .build();
            return new ResponseEntity<>(responseMessage,HttpStatus.UNAUTHORIZED);
        }else {
            APIResponseMessage responseMessage = new APIResponseMessage().builder()
                    .message(exp.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .code(HttpStatusCode.valueOf(400))
                    .build();
            return new ResponseEntity<>(responseMessage,HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<APIResponseMessage> handleTaskException(TaskException exp){
        logger.error("Exception : Error in Task : {}",exp.getMessage(),TaskException.class);
        APIResponseMessage responseMessage = new APIResponseMessage().builder()
                .message(exp.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatusCode.valueOf(500))
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
