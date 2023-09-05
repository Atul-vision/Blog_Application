package com.services.blog.services;

import java.util.List;

import com.services.blog.entities.User;
import com.services.blog.payloads.UserDto;

public interface UserService {
	
	UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer usedId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);
	
}
