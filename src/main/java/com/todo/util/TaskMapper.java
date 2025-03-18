package com.todo.util;

import com.todo.dto.TaskDto;
import com.todo.dto.TaskResponseDto;
import com.todo.entity.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaskMapper {

    public Task toTask(TaskDto dto){
        return new Task(dto.title(), dto.description(),dto.status(),dto.priority());
    }

    public TaskResponseDto toTaskResponseDto(Task task){
        return new TaskResponseDto(task.getId(),task.getTitle(), task.getDescription(), task.getStatus(),task.getPriority(), task.getCreatedAt().toLocalDate());
    }

}
