//package com.todo.service;
//
//import com.todo.dto.TaskDto;
//import com.todo.dto.TaskResponseDto;
//import com.todo.entity.Priority;
//import com.todo.entity.Status;
//import com.todo.entity.Task;
//import com.todo.entity.User;
//import com.todo.repository.TaskRepository;
//import com.todo.util.TaskMapper;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class TaskServiceTest {
//
//    @InjectMocks
//    private TaskService service;
//    @Mock
//    private TaskRepository repository;
//    @Mock
//    private TaskMapper mapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void checkGetAll(){
//        User user = new User().builder().email("sndp@mail.com").password("password").build();
//        Task task = new Task().builder().title("Home Work").description("Math and Science").status(Status.PENDING).priority(Priority.MEDIUM).user(user).build();
//        task.setId(1);
//        List<Task> list = List.of(task);
//        LocalDateTime dateTime = LocalDateTime.now();
//
//        Mockito.when(repository.findAll()).thenReturn(list);
//        Mockito.when(mapper.toTaskResponseDto(Mockito.any(Task.class))).thenReturn(new TaskResponseDto(1,"Home Work","Math and Science", Status.PENDING, Priority.MEDIUM,dateTime.toLocalDate(),"sndp@mail.com"));
//
//        List<TaskResponseDto> responseDtos = service.getAll();
//
//        Assertions.assertEquals(responseDtos.get(0).title(),task.getTitle());
//        Assertions.assertEquals(responseDtos.get(0).description(),task.getDescription());
//        Assertions.assertEquals(responseDtos.get(0).status(),task.getStatus());
//        Assertions.assertEquals(responseDtos.get(0).priority(),task.getPriority());
//        Assertions.assertEquals(responseDtos.get(0).user(),task.getUser().getEmail());
//
//        Mockito.verify(repository,Mockito.times(1)).findAll();
//    }
//
//    @Test
//    public void checkAdd(){
//        TaskDto dto = new TaskDto("Home Work","Math and Science", Status.PENDING, Priority.MEDIUM);
//        User user = new User().builder().email("sndp@mail.com").password("password").build();
//        Task task = new Task().builder().title("Home Work").description("Math and Science").status(Status.PENDING).priority(Priority.MEDIUM).user(user).build();
//        LocalDateTime dateTime = LocalDateTime.now();
//        task.setCreatedAt(dateTime);
//        TaskResponseDto responseDto = new TaskResponseDto(1,"Home Work","Math and Science", Status.PENDING, Priority.MEDIUM,dateTime.toLocalDate(),"sndp@mail.com");
//        Task saveTask = new Task().builder().title("Home Work").description("Math and Science").status(Status.PENDING).priority(Priority.MEDIUM).user(user).build();
//        saveTask.setId(1);
//        saveTask.setCreatedAt(dateTime);
//
//        Mockito.when(mapper.toTask(dto)).thenReturn(task);
//        Mockito.when(repository.save(task)).thenReturn(saveTask);
//        Mockito.when(mapper.toTaskResponseDto(saveTask)).thenReturn(responseDto);
//
//        TaskResponseDto taskResponseDto = service.add(dto);
//
//        Assertions.assertEquals(taskResponseDto.title(),dto.title());
//        Assertions.assertEquals(taskResponseDto.description(),dto.description());
//        Assertions.assertEquals(taskResponseDto.status(),dto.status());
//        Assertions.assertEquals(taskResponseDto.priority(),dto.priority());
////        Assertions.assertEquals(taskResponseDto.user(),);
//
//        Mockito.verify(repository,Mockito.times(1)).save(task);
//    }
//
//    @Test
//    public void checkUpdate(){
//        Integer taskId = 1;
//        TaskDto dto = new TaskDto("Home Work","Math and Science", Status.PENDING, Priority.HIGH);
//        LocalDateTime dateTime = LocalDateTime.now();
//        Task task = new Task().builder().title("Home Work").description("Math and Science").status(Status.PENDING).priority(Priority.MEDIUM).build();
//        task.setCreatedAt(dateTime);
//        Task saveTask = new Task().builder().title(dto.title()).description(dto.description()).status(dto.status()).priority(dto.priority()).build();
//        saveTask.setCreatedAt(task.getCreatedAt());
//        saveTask.setId(taskId);
////        System.out.println(saveTask+"Save Task");
//        TaskResponseDto responseDto= new TaskResponseDto(saveTask.getId(),saveTask.getTitle(),saveTask.getDescription(), saveTask.getStatus(), saveTask.getPriority(),saveTask.getCreatedAt().toLocalDate(),"sndp@mail.com");
////        System.out.println(responseDto);
//        Mockito.when(repository.findById(taskId)).thenReturn(Optional.of(task));
//        Mockito.when(repository.save(task)).thenReturn(saveTask);
//        Mockito.when(mapper.toTaskResponseDto(saveTask)).thenReturn(responseDto);
//
//        TaskResponseDto taskResponseDto = service.update(taskId,dto);
//        System.out.println(taskResponseDto);
//        Assertions.assertEquals(taskResponseDto.title(),dto.title());
//        Assertions.assertEquals(taskResponseDto.description(),dto.description());
//        Assertions.assertEquals(taskResponseDto.status(),dto.status());
//        Assertions.assertEquals(taskResponseDto.priority(),dto.priority());
//
//        Mockito.verify(repository,Mockito.times(1)).findById(taskId);
//        Mockito.verify(repository,Mockito.times(1)).save(task);
//    }
//
//    @Test
//    public void checkDelete(){
//        Integer taskId = 1;
//        Task task = new Task().builder().title("Home Work").description("Math and Science").status(Status.PENDING).priority(Priority.MEDIUM).build();
//        task.setId(1);
//        task.setActive(true);
//
//        Task saveTask = new Task().builder().title("Home Work").description("Math and Science").status(Status.PENDING).priority(Priority.HIGH).build();
//        saveTask.setId(taskId);
//        saveTask.setActive(false);
//
//        Mockito.when(repository.findById(taskId)).thenReturn(Optional.of(task));
//        Mockito.when(repository.save(task)).thenReturn(saveTask);
//
//        boolean isActive = service.delete(taskId);
//
//        Assertions.assertTrue(isActive);
//
//        Mockito.verify(repository,Mockito.times(1)).findById(taskId);
//        Mockito.verify(repository,Mockito.times(1)).save(task);
//
//    }
//
//}