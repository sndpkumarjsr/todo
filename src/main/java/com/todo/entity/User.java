package com.todo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class User extends BaseEntity{
    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty(message = "Email must required")
    @NotNull(message = "Email should not be null")
    @Email
    @Column(unique = true)
    private String email;
    @NotEmpty(message = "Password must required")
    @NotNull(message = "Password should not be null")
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;
}
