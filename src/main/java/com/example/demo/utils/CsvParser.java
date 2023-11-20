package com.example.demo.utils;

import com.example.demo.dto.TodoItemCsv;
import com.example.demo.entity.Todo;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CsvParser {

    public Set<Todo> parseCsv(final MultipartFile file) {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            HeaderColumnNameMappingStrategy<TodoItemCsv> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(TodoItemCsv.class);
            CsvToBean<TodoItemCsv> csvToBean = new CsvToBeanBuilder<TodoItemCsv>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse()
                    .stream()
                    .map(csv -> Todo
                            .builder()
                            .title(csv.getTitle())
                            .status(csv.getStatus())
                            .createdBy(csv.getCreatedBy())
                            .build()
                    ).collect(Collectors.toSet());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
