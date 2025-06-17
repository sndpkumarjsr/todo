package com.todo.service;

import com.todo.dto.TaskDto;
import com.todo.dto.TaskResponseDto;
import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.entity.Task;
import com.todo.entity.User;
import com.todo.exception.TaskException;
import com.todo.repository.TaskRepository;
import com.todo.repository.UserRepository;
import com.todo.util.TaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository repository, TaskMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "tasks", key = "#email")
    public List<TaskResponseDto> getAll(String email){
        logger.info("Service: Fetching all active tasks for email: {}", email);

        List<TaskResponseDto> tasks = repository.findAll()
                .stream()
                .filter(task -> task.isActive() && task.getUser().getEmail().equals(email))
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());

        logger.debug("Found {} tasks for email: {}", tasks.size(), email);
        return tasks;
    }

    @CachePut(value = "tasks", key = "#dto.userEmail")
    @Transactional
    public TaskResponseDto add(TaskDto dto) throws TaskException{
        logger.info("Service: Adding task for user: {}", dto.userEmail());

        Optional<User> userOpt = userRepository.findByEmail(dto.userEmail());
        if (userOpt.isEmpty()) {
            logger.error("Task creation failed: No user found with email: {}", dto.userEmail());
            throw new TaskException("Something went wrong. Please try again.");
        }

        Task task = mapper.toTask(dto);
        User user = userOpt.get();
        task.setUser(user);
        task.setCreatedBy(user.getEmail());
        task.setCreatedAt(LocalDateTime.now());
        task.setActive(true);

        Task savedTask = repository.save(task);
        logger.debug("Task successfully created with ID: {}", savedTask.getId());
        return mapper.toTaskResponseDto(savedTask);
    }

    @CachePut(value = "tasks", key = "#email + ':' + #taskId")
    @Transactional
    public TaskResponseDto update(Integer taskId,String email,TaskDto dto) throws TaskException{
        logger.info("Service: Updating task ID: {} for email: {}", taskId, email);

        Optional<Task> taskOpt = repository.findById(taskId);
        if (taskOpt.isEmpty()) {
            logger.warn("Update failed: Task not found with ID: {}", taskId);
            throw new TaskException("Task not found.");
        }

        Task task = taskOpt.get();

        if (!task.isActive()) {
            logger.warn("Update failed: Task ID {} is inactive", taskId);
            throw new TaskException("Cannot update inactive task.");
        }

        if (task.getUser() == null || !task.getUser().getEmail().equals(email)) {
            logger.warn("Update failed: Task ID {} does not belong to user {}", taskId, email);
            throw new TaskException("Cannot update task: unauthorized access");
        }

        task.setPriority(dto.priority());
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setModifiedAt(LocalDateTime.now());

        Task updatedTask = repository.save(task);
        logger.debug("Task updated successfully: ID = {}", updatedTask.getId());
        return mapper.toTaskResponseDto(updatedTask);
    }

    @Transactional
    public boolean delete(Integer taskId,String email){
        logger.info("Service: Deleting task ID: {} for user: {}", taskId, email);

        Optional<Task> taskOpt = repository.findById(taskId);
        if (taskOpt.isEmpty()) {
            logger.warn("Delete failed: Task ID {} not found", taskId);
            return false;
        }

        Task task = taskOpt.get();

        if (task.getUser() == null || !task.getUser().getEmail().equals(email)) {
            logger.warn("Delete failed: Task ID {} does not belong to user {}", taskId, email);
            return false;
        }

        task.setActive(false);
        task.setModifiedAt(LocalDateTime.now());
        repository.save(task);

        logger.debug("Task soft-deleted successfully: ID = {}", taskId);
        return true;
    }
    @Cacheable(value = "tasks", key = "#email + ':' + #status")
    public List<TaskResponseDto> getByStatus(Status status,String email){
        logger.info("Service: Fetching tasks with status: {} for email: {}", status, email);

        List<TaskResponseDto> tasks = repository.findByStatus(status)
                .stream()
                .filter(task -> task.isActive() && task.getUser().getEmail().equals(email))
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());

        logger.debug("Found {} tasks with status={} for user={}", tasks.size(), status, email);
        return tasks;
    }
    @Cacheable(value = "tasks", key = "#email + ':' + #priority")
    public List<TaskResponseDto> getByPriority(Priority priority,String email){
        logger.info("Service: Fetching tasks with priority: {} for email: {}", priority, email);

        List<TaskResponseDto> tasks = repository.findByPriority(priority)
                .stream()
                .filter(task -> task.isActive() && task.getUser().getEmail().equals(email))
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());

        logger.debug("Found {} tasks with priority={} for user={}", tasks.size(), priority, email);
        return tasks;
    }
}
