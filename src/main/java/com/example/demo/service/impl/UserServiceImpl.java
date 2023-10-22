package com.example.demo.service.impl;

import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    @Override
    public List<User> findAll() {
        List<User> results = repo.findAll();
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }

        return results;
    }

    @Override
    public User findById(String id) {
        return repo.findById(id)
                .orElse(new User());
    }

    @Override
    public UserResponseDto getLoggedInUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(auth)
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            if (Objects.nonNull(auth.getPrincipal()))
                return convertToUserResponseDto(auth.getPrincipal());
        }

        return null;
    }

    @Override
    public User findByLastName(String lastName) {
        return repo.findByLastName(lastName)
                .orElse(new User());
    }

    private UserResponseDto convertToUserResponseDto(Object userObj) {
        User user = (User) userObj;
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}