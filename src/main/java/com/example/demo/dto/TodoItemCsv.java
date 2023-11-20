package com.example.demo.dto;

import com.example.demo.entity.Status;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoItemCsv {
    @CsvBindByName(column = "title")
    private String title;
    @CsvBindByName(column = "status")
    private Status status;
    @CsvBindByName(column = "createdBy")
    private String createdBy;
}
