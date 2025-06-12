package com.todo.util;

import com.todo.dto.UserDto;
import com.todo.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp(){
        mapper = new UserMapper();
    }

    @Test
    public void getUserTest(){
        UserDto dto = new UserDto("sndp@mail.com","abc@1234");
        User user = mapper.getUser(dto);

        Assertions.assertEquals(user.getEmail(),dto.email());
        Assertions.assertEquals(user.getPassword(),dto.password());
    }

    @Test
    public void getUserDtoTest(){
        User user = new User()
                .builder()
                .email("sndp@mail.com")
                .password("abc@1234")
                .build();
        UserDto dto = mapper.getUserDto(user);

        Assertions.assertEquals(dto.email(),user.getEmail());
        Assertions.assertEquals(dto.password(),user.getPassword());
    }
}