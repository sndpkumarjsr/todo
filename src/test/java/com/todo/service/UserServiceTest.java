package com.todo.service;

import com.todo.dto.UserDto;
import com.todo.entity.User;
import com.todo.repository.UserRepository;
import com.todo.util.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class UserServiceTest {

    @InjectMocks
    private UserService service;

    private UserMapper mapper = new UserMapper(); // FIXED: Manually instantiate instead of @InjectMocks

    @Mock
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject the mapper manually into the service
        service = new UserService(repository, mapper);
    }

    @Test
    public void checkAuthenticate() {
        String email = "sndp@mail.com";
        String password = "password";

        User user = User.builder().email(email).password(password).build();

        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        boolean isValid = service.authenticate(email, password);

        Assertions.assertTrue(isValid);

        Mockito.verify(repository, Mockito.times(1)).findByEmail(email);
    }

    @Test
    public void checkAdd() {
        // Arrange
        UserDto dto = new UserDto("sndp@mail.com", "abc@1234");
        User user = User.builder().email(dto.email()).password(dto.password()).build();
        User savedUser = User.builder().email(dto.email()).password(dto.password()).build();
        savedUser.setId(1);

        // Mocks
        Mockito.when(repository.findByEmail(dto.email())).thenReturn(Optional.empty());
        Mockito.when(repository.save(user)).thenReturn(savedUser);

        // Act
        User responseUser = service.add(dto);

        // Assert
        Assertions.assertNotNull(responseUser);
        Assertions.assertEquals(dto.email(), responseUser.getEmail());
        Assertions.assertEquals(dto.password(), responseUser.getPassword());

        // Verify
        Mockito.verify(repository, Mockito.times(1)).findByEmail(dto.email());
        Mockito.verify(repository, Mockito.times(1)).save(user);
    }
}
