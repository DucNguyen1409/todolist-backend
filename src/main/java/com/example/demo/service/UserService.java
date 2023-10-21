package com.example.demo.service;


import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

   User findById(String id);

    UserResponseDto getLoggedInUser();

}
