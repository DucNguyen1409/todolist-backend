package com.example.demo.dto;

import com.example.demo.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDto {

    private String id;
    private String title;
    private Status status;
    private Date createdDate;
    private String createdBy;

}
