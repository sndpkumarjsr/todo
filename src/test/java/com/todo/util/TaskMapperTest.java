package com.todo.util;

import com.todo.dto.TaskDto;
import com.todo.dto.TaskResponseDto;
import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.entity.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class TaskMapperTest {

    private TaskMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TaskMapper();
    }

    @Test
    public void checkToTask(){
        TaskDto dto = new TaskDto("Home Work","Math and Science",Status.PENDING, Priority.MEDIUM);

        Task task = mapper.toTask(dto);

        Assertions.assertEquals(task.getTitle(),dto.title());
        Assertions.assertEquals(task.getDescription(),dto.description());
        Assertions.assertEquals(task.getPriority(),dto.priority());
    }

    @Test
    public void checkToTaskResponseDto(){
        Task task = new Task().builder().title("Home Work").description("Math and Science").status(Status.PENDING).priority(Priority.MEDIUM).build();
        task.setId(1);
        task.setCreatedAt(LocalDateTime.now());

        TaskResponseDto responseDto = mapper.toTaskResponseDto(task);

        Assertions.assertEquals(responseDto.title(),task.getTitle());
        Assertions.assertEquals(responseDto.description(),task.getDescription());
        Assertions.assertEquals(responseDto.status(),task.getStatus());
        Assertions.assertEquals(responseDto.priority(),task.getPriority());
        Assertions.assertEquals(responseDto.createdAt(),task.getCreatedAt().toLocalDate());

    }

}