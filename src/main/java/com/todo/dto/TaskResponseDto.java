package com.todo.dto;

import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.entity.User;

import java.time.LocalDate;

public record TaskResponseDto(
        Integer id,
        String title,
        String description,
        Status status,
        Priority priority,
        LocalDate createdAt,
        String user
) {
}
