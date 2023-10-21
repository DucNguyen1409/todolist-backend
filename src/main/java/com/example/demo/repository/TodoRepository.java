package com.example.demo.repository;

import com.example.demo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, String> {

    Todo findByTitleAndCreatedBy(final String title, final String createdBy);

    List<Todo> findByCreatedBy(final String createdBy);

}
