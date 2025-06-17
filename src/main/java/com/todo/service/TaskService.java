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
    private static Logger logger = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository repository, TaskMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Cacheable(value = "tasks", key = "#email")
    public List<TaskResponseDto> getAll(String email){
        logger.info("Service : Get All Active Task of Email : {}",email);
        return repository.findAll()
                .stream().filter(i->i.isActive() && i.getUser().getEmail().equals(email))
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }

    @CachePut(value = "tasks", key = "#dto.userEmail")
    @Transactional
    public TaskResponseDto add(TaskDto dto) throws TaskException{
        logger.info("Service : Add Task : {}",dto);
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
        throw new TaskException("Something went wrong. Please try again.");
    }

    @CachePut(value = "tasks", key = "#email + ':' + #taskId")
    @Transactional
    public TaskResponseDto update(Integer taskId,String email,TaskDto dto) throws TaskException{
        logger.info("Service : Update Task : {}",dto);
        Optional<Task> taskOpt = repository.findById(taskId);
        if(taskOpt.isPresent() && taskOpt.get().getUser() == null){
            logger.error("Service : Error in Task Id : {}, Task : {} ",taskId,dto,TaskException.class);
            throw new TaskException("Cannot update task: user does not exist");
        }
        if(taskOpt.isPresent() && taskOpt.get().isActive() && taskOpt.get().getUser().getEmail().equals(email)){
            Task task = taskOpt.get();
            task.setPriority(dto.priority());
            task.setTitle(dto.title());
            task.setDescription(dto.description());
            task.setStatus(dto.status());
            task.setModifiedAt(LocalDateTime.now());

            Task savedTask = repository.save(task);
            return mapper.toTaskResponseDto(savedTask);
        }
        throw new TaskException("Something went wrong. Please try again.");
    }

    @Transactional
    public boolean delete(Integer taskId,String email){
        logger.info("Service : Delete Task : {}, and email : {}",taskId,email);
        Optional<Task> taskOpt = repository.findById(taskId);
        if(taskOpt.isPresent() && taskOpt.get().getUser().getEmail().equals(email)){
            Task task = taskOpt.get();
            task.setActive(false);
            task.setModifiedAt(LocalDateTime.now());
            Task deleteTask = repository.save(task);
            return true;
        }
        return false;
    }
    @Cacheable(value = "tasks", key = "#email + ':' + #status")
    public List<TaskResponseDto> getByStatus(Status status,String email){
        logger.info("Service : Get All Active Task Based on Status : {} and Email : {}",status,email);
        return repository.findByStatus(status)
                .stream().filter(i->i.isActive() && i.getUser().equals(email))
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }
    @Cacheable(value = "tasks", key = "#email + ':' + #priority")
    public List<TaskResponseDto> getByPriority(Priority priority,String email){
        logger.info("Service : Get All Active Task Based on Priority : {} and Email : {}",priority,email);
        return repository.findByPriority(priority)
                .stream().filter(i->i.isActive() && i.getUser().getEmail().equals(email))
                .map(mapper::toTaskResponseDto)
                .collect(Collectors.toList());
    }
}
