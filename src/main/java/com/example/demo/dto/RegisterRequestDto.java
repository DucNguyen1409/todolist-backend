package com.example.demo.dto;

import com.example.demo.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    private String email;

    @NotEmpty(message = "password cannot be empty")
    private String password;

    private Role role;

}
