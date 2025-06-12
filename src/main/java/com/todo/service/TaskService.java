package com.todo.service;

import com.todo.dto.TaskDto;
import com.todo.dto.TaskResponseDto;
import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.entity.Task;
import com.todo.entity.User;
import com.todo.repository.TaskRepository;
import com.todo.repository.UserRepository;
import com.todo.util.TaskMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final UserRepository userRepository;

    public TaskService(TaskRepository repository, TaskMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public List<TaskResponseDto> getAll(){
        return repository.findAll()
                .stream().filter(i->i.isActive())
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }

    @CachePut(value = "tasks", key = "#dto.userEmail")
    public TaskResponseDto add(TaskDto dto){
        Task task = mapper.toTask(dto);
        Optional<User> userOpt =  userRepository.findByEmail(dto.userEmail());
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            task.setUser(user);
            task.setCreatedAt(LocalDateTime.now());
            task.setActive(true);

            Task saveTask = repository.save(task);
            return mapper.toTaskResponseDto(saveTask);
        }
        return null;
    }
    @CachePut(value = "tasks", key = "#dto.userEmail")
    public TaskResponseDto update(Integer taskId,TaskDto dto){
        Optional<Task> taskOpt = repository.findById(taskId);
        Optional<User> userOpt = userRepository.findByEmail(dto.userEmail());
        if(taskOpt.isPresent() && taskOpt.get().isActive() && userOpt.isPresent()){
            Task task = taskOpt.get();
            task.setPriority(dto.priority());
            task.setTitle(dto.title());
            task.setDescription(dto.description());
            task.setStatus(dto.status());
            task.setModifiedAt(LocalDateTime.now());

            Task savedTask = repository.save(task);
            return mapper.toTaskResponseDto(savedTask);
        }
        return null;
    }

    public boolean delete(Integer taskId){
        Optional<Task> taskOpt = repository.findById(taskId);
        if(taskOpt.isPresent()){
            Task task = taskOpt.get();
            task.setActive(false);
            task.setModifiedAt(LocalDateTime.now());
            Task deleteTask = repository.save(task);
            return true;
        }
        return false;
    }
    @Cacheable(value = "tasks",key = "#status")
    public List<TaskResponseDto> getByStatus(Status status){
        return repository.findByStatus(status)
                .stream().filter(i->i.isActive())
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }
    @Cacheable(value = "tasks",key = "#priority")
    public List<TaskResponseDto> getByPriority(Priority priority){
        return repository.findByPriority(priority)
                .stream().filter(i->i.isActive())
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }
}
