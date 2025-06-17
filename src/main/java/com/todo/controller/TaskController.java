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
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<TaskResponseDto>> getAll(@PathVariable String email){
        logger.info("GET /tasks/{} - Fetching all tasks for email", email);
        List<TaskResponseDto> tasks = service.getAll(email);
        logger.debug("Fetched {} tasks for user: {}", tasks.size(), email);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<?> addNewTask(@Valid @RequestBody TaskDto taskDto) throws TaskException {
        logger.info("POST /tasks - Adding new task: {}", taskDto);
        TaskResponseDto response = service.add(taskDto);
        logger.debug("Task created with ID: {}", response.id());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("{taskId}/{email}")
    public ResponseEntity<?> updateTask(@PathVariable Integer taskId,@PathVariable String email, @Valid @RequestBody TaskDto taskDto) throws TaskException{
        logger.info("PUT /tasks/{}/{} - Updating task with data: {}", taskId, email, taskDto);
        TaskResponseDto updatedTask = service.update(taskId, email, taskDto);
        logger.debug("Task updated with ID: {}", updatedTask.id());
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("{taskId}/{email}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId,@PathVariable String email){
        logger.info("DELETE /tasks/{}/{} - Attempting to delete task", taskId, email);

        boolean deleted = service.delete(taskId, email);

        if (deleted) {
            logger.debug("Task successfully deleted: ID={}, email={}", taskId, email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.warn("Task deletion failed: ID={}, email={}", taskId, email);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status/{status}/{email}")
    public ResponseEntity<List<TaskResponseDto>> getByStatus(@PathVariable Status status, @PathVariable String email){
        logger.info("GET /tasks/status/{}/{} - Fetching tasks by status", status, email);
        List<TaskResponseDto> tasks = service.getByStatus(status, email);
        logger.debug("Found {} tasks with status={} for user={}", tasks.size(), status, email);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/priority/{priority}/{email}")
    public ResponseEntity<List<TaskResponseDto>> getByPriority(@PathVariable Priority priority, @PathVariable String email){
        logger.info("GET /tasks/priority/{}/{} - Fetching tasks by priority", priority, email);
        List<TaskResponseDto> tasks = service.getByPriority(priority, email);
        logger.debug("Found {} tasks with priority={} for user={}", tasks.size(), priority, email);
        return ResponseEntity.ok(tasks);
    }

}
