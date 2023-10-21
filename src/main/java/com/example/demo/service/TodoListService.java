package com.example.demo.service;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.Status;
import com.example.demo.entity.Todo;

import java.util.List;

public interface TodoListService {

    List<TodoDto> findAll();

    List<TodoDto> findByUserCreated(final String createdBy);

    TodoDto findById(final String id);

    boolean existByTitleAndUserCreated(final String title, final String createdBy);

    TodoDto updateStatus(final String id, final Status status);

    String saveToDo(final Todo todo);


}
