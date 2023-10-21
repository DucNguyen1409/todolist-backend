package com.example.demo.dto;

import com.example.demo.entity.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoRequestDto {

    @NotEmpty(message = "title cannot be empty")
    private String title;

    @NotNull(message = "status cannot be null")
    private Status status;

    @NotEmpty(message = "createdBy cannot be empty")
    private String createdBy;

}
