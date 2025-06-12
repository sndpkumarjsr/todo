package com.todo.util;

import com.todo.dto.UserDto;
import com.todo.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User getUser(UserDto dto){
        return new User().builder().email(dto.email()).password(dto.password()).build();
    }
    public UserDto getUserDto(User user){
        return new UserDto(user.getEmail(), user.getPassword());
    }
}
