package com.todo.service;

import com.todo.entity.User;
import com.todo.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkAuthenticate(){
        String email = "sndp@mail.com";
        String password = "password";

        User user = new User("sndp@mail.com","password");

        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        boolean isValiid = service.authenticate(email,password);

        Assertions.assertTrue(isValiid);

        Mockito.verify(repository,Mockito.times(1)).findByEmail(email);
    }

    @Test
    public void checkAdd(){
        User user = new User("sndp@mail.com","password");
        User savedUser = new User("sndp@mail.com","password");
        savedUser.setId(1);

        Mockito.when(repository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
//        Mockito.when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(repository.save(user)).thenReturn(savedUser);

        User responseUser = service.add(user);

//        Assertions.assertNull(responseUser);
        Assertions.assertEquals(responseUser.getEmail(),user.getEmail());
        Assertions.assertEquals(responseUser.getPassword(),user.getPassword());

        Mockito.verify(repository,Mockito.times(1)).findByEmail(user.getEmail());
        Mockito.verify(repository,Mockito.times(1)).save(user);;
    }
}