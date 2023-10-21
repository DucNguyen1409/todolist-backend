package com.example.demo.service.impl;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.Status;
import com.example.demo.entity.Todo;
import com.example.demo.entity.User;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoListService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToDoListServiceImpl implements TodoListService {

    private final TodoRepository repo;

    private final UserService userService;

    /**
     * Find all
     *
     * @return TodoDto list
     */
    @Override
    public List<TodoDto> findAll() {
        var results = repo.findAll();
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }

        return results.stream()
                .map(this::convertToTodoDto)
                .collect(Collectors.toList());
    }

    /**
     * Find by user created
     *
     * @param createdBy created by
     * @return TodoDto list
     */
    @Override
    public List<TodoDto> findByUserCreated(String createdBy) {
        var results = repo.findByCreatedBy(createdBy);
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyList();
        }

        return results.stream()
                .map(this::convertToTodoDto)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDto findById(String id) {
        var entity = repo.findById(id)
                .orElse(new Todo());

        return convertToTodoDto(entity);
    }

    /**
     * exist check by title and user created
     *
     * @param title title
     * @param createdBy createdBy
     * @return true if exist and vice versa
     */
    @Override
    public boolean existByTitleAndUserCreated(String title, String createdBy) {
        return Objects.nonNull(repo.findByTitleAndCreatedBy(title, createdBy));
    }

    /**
     * Update status by id
     *
     * @param id To-do ID
     * @param status Status
     * @return TodoDto
     */
    @Override
    public TodoDto updateStatus(String id, Status status) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ApiRequestException("Not found item"));
        entity.setStatus(status);

        // update
        return convertToTodoDto(repo.save(entity));
    }


    /**
     * Create To-do item
     *
     * @param todo To-do
     * @return To-do ID
     */
    @Override
    public String saveToDo(Todo todo) {
        // user check
        User userCreatedBy = userService.findById(todo.getCreatedBy());
        if (Objects.isNull(userCreatedBy.getId())) {
            throw new ApiRequestException("User not found: " + todo.getCreatedBy());
        }

        todo.setCreatedBy(todo.getCreatedBy());
        todo.setCreatedDate(new Date());

        return repo.save(todo).getId();
    }

    private TodoDto convertToTodoDto(Todo entity) {
        return TodoDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .createdDate(entity.getCreatedDate())
                .build();
    }

}
