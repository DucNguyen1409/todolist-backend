package com.example.demo.service;

import com.example.demo.dto.TodoDto;
import com.example.demo.entity.Status;
import com.example.demo.entity.Todo;

import java.util.List;

public interface TodoListService {

    /**
     * Find all
     *
     * @return TodoDto list
     */
    List<TodoDto> findAll();

    /**
     * Find by user created
     *
     * @param createdBy created by
     * @return TodoDto list
     */
    List<TodoDto> findByUserCreated(final String createdBy);

    TodoDto findById(final String id);

    /**
     * exist check by title and user created
     *
     * @param title title
     * @param createdBy createdby
     * @return true if exist and vice versa
     */
    boolean existByTitleAndUserCreated(final String title, final String createdBy);

    /**
     * Update status by id
     *
     * @param id To-do ID
     * @param status Status
     * @return TodoDto
     */
    TodoDto updateStatus(final String id, final Status status);

    /**
     * Create To-do item
     *
     * @param todo To-do
     * @return To-do ID
     */
    String saveToDo(final Todo todo);

    /**
     * Delete To-do item
     *
     * @param id to-do ID
     */
    void deleteToDo(final String id);

}
