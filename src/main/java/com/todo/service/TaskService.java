package com.todo.service;

import com.todo.dto.TaskDto;
import com.todo.dto.TaskResponseDto;
import com.todo.entity.Priority;
import com.todo.entity.Status;
import com.todo.entity.Task;
import com.todo.repository.TaskRepository;
import com.todo.util.TaskMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<TaskResponseDto> getAll(){
        return repository.findAll()
                .stream().filter(i->i.isActive())
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }


    public TaskResponseDto add(TaskDto dto){
        Task task = mapper.toTask(dto);
        task.setCreatedAt(LocalDateTime.now());
        task.setActive(true);

        Task saveTask = repository.save(task);
        return mapper.toTaskResponseDto(saveTask);
    }

    public TaskResponseDto update(Integer taskId,TaskDto dto){
        Optional<Task> taskOpt = repository.findById(taskId);
        if(taskOpt.isPresent() && taskOpt.get().isActive()){
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

    public List<TaskResponseDto> getByStatus(Status status){
        return repository.findByStatus(status)
                .stream().filter(i->i.isActive())
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDto> getByPriority(Priority priority){
        return repository.findByPriority(priority)
                .stream().filter(i->i.isActive())
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }
}
