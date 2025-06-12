package com.todo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        @NotNull(message = "Email should not be null")
                @NotEmpty(message = "Email should not be empty")
                @Email
        String email,
        @NotNull(message = "Password should not be null")
                @NotEmpty(message = "Password should not be empty")
        String password
) {
}
