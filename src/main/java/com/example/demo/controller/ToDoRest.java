package com.example.demo.controller;

import com.example.demo.dto.TodoDto;
import com.example.demo.dto.TodoRequestDto;
import com.example.demo.entity.Status;
import com.example.demo.entity.Todo;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.service.TodoListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/v1/todos")
@RequiredArgsConstructor
public class ToDoRest {

    private final TodoListService todoListService;

    @GetMapping
    public ResponseEntity<List<TodoDto>> findAll() {
        log.info("[ToDoRest] findAll");
        return ResponseEntity.ok().body(todoListService.findAll());
    }

    @GetMapping("/by-user/{user}")
    public ResponseEntity<List<TodoDto>> findByUser(@PathVariable String user) {
        log.info("[ToDoRest] findByUser: " + user);
        return ResponseEntity.ok().body(todoListService.findByUserCreated(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TodoDto> updateStatus(@PathVariable String id,
                                                @RequestParam(value = "status") String status) {
        log.info("[ToDoRest] updateStatus: {}, {}", id, status);
        return ResponseEntity.ok().body(todoListService.updateStatus(id, Status.valueOf(status)));
    }

    @PostMapping
    public ResponseEntity<String> createToDoItem(@Valid @RequestBody TodoRequestDto dto) {
        log.info("[ToDoRest] createToDoItem: " + dto);
        // exist to-do check
        boolean existByTitle = todoListService.existByTitleAndUserCreated(dto.getTitle(), dto.getCreatedBy());
        if (existByTitle) {
            throw new ApiRequestException("exist by item: " + dto.getTitle());
        }

        return new ResponseEntity<>(todoListService.saveToDo(convertToTodo(dto)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodoItem(@PathVariable String id,
                                                  @Valid @RequestBody TodoRequestDto dto) {
        log.info("[ToDoRest] updateTodoItem: {}, {} ", id, dto);
        // exist to-do check
        var existEntity = todoListService.findById(id);
        if (Objects.isNull(existEntity.getId())) {
            throw new ApiRequestException("Not found item: " + id);
        }

        existEntity.setTitle(dto.getTitle());
        return new ResponseEntity<>(todoListService.updateTodo(convertToTodo(existEntity)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteToDoItem(@PathVariable String id) {
        log.info("[ToDoRest] deleteToDoItem: " + id);

        todoListService.deleteToDo(id);
        return ResponseEntity.ok().build();
    }

    private Todo convertToTodo(TodoRequestDto dto) {
        return Todo.builder()
                .title(dto.getTitle())
                .status(dto.getStatus())
                .createdBy(dto.getCreatedBy())
                .build();
    }

    private Todo convertToTodo(TodoDto dto) {
        return Todo.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .createdBy(dto.getCreatedBy())
                .build();
    }

}
