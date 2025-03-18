package com.todo.dto;

import com.todo.entity.Priority;
import com.todo.entity.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskDto(
        @NotEmpty(message = "Title must required")
        @NotNull(message = "Title should not be null")
        String title,
        @NotEmpty(message = "Description must requied")
        @NotNull(message = "Description should not be null")
        String description,
//        @NotEmpty(message = "status must required")
//        @NotNull(message = "Status should not be null")
        Status status,
//        @NotEmpty(message = "Priority must required")
//        @NotNull(message = "Priority should not be null")
        Priority priority
) {

}
