package com.todo.controller;

import com.todo.dto.TaskDto;
import com.todo.dto.TaskResponseDto;
import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAll(){
        var list = service.getAll();
        if(list.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<?> addNewTask(@Valid @RequestBody TaskDto taskDto){
        var response = service.add(taskDto);
        if(response == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Integer taskId, @Valid @RequestBody TaskDto taskDto){
        var response = service.update(taskId,taskDto);
        if(response == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId){
        boolean isDeleted = service.delete(taskId);
        if(isDeleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getByStatus(@PathVariable Status status){
        var list = service.getByStatus(status);
        if(list.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponseDto>> getByPriority(@PathVariable Priority priority){
        var list = service.getByPriority(priority);
        if(list.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(list);
    }
}
