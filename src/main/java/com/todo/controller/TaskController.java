package com.todo.controller;

import com.todo.dto.TaskDto;
import com.todo.dto.TaskResponseDto;
import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.exception.TaskException;
import com.todo.service.TaskService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;
    private static Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<TaskResponseDto>> getAll(@PathVariable String email){
        logger.info("Controller : Get all task of email : {}",email);
        return ResponseEntity.ok(service.getAll(email));
    }

    @PostMapping
    public ResponseEntity<?> addNewTask(@Valid @RequestBody TaskDto taskDto) throws TaskException {
        logger.info("Controller : Add Task : {}",taskDto);
        return ResponseEntity.ok(service.add(taskDto));
    }

    @PutMapping("{taskId}/{email}")
    public ResponseEntity<?> updateTask(@PathVariable Integer taskId,@PathVariable String email, @Valid @RequestBody TaskDto taskDto) throws TaskException{
        logger.info("Controller : Update Task : {}",taskDto);
        return ResponseEntity.ok(service.update(taskId,email,taskDto));
    }

    @DeleteMapping("{taskId}/{email}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId,@PathVariable String email){
        logger.info("Controller : Delete Task ID : {} and Email : {}",taskId,email);
        return (service.delete(taskId,email))? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/status/{status}/{email}")
    public ResponseEntity<List<TaskResponseDto>> getByStatus(@PathVariable Status status, @PathVariable String email){
        logger.info("Controller : Get All By Status : {} and email : {}",status,email);
        return ResponseEntity.ok(service.getByStatus(status,email));
    }

    @GetMapping("/priority/{priority}/{email}")
    public ResponseEntity<List<TaskResponseDto>> getByPriority(@PathVariable Priority priority, @PathVariable String email){
        logger.info("Controller : Get All By Priority : {} and email : {}",priority,email);
        return ResponseEntity.ok(service.getByPriority(priority,email));
    }

}
