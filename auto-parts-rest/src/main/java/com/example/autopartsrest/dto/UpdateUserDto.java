package com.example.autopartsrest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {
    private String name;
    private String surname;
    private String email;
    private int phone;
    private String password;
}
