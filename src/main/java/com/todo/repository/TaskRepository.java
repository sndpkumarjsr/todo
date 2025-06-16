package com.todo.repository;

import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.entity.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {

    @EntityGraph(attributePaths = "user")
    List<Task> findByStatus(Status status);

    @EntityGraph(attributePaths = "user")
    List<Task> findByPriority(Priority priority);

    @EntityGraph(attributePaths = "user")
    List<Task> findAll();
}
