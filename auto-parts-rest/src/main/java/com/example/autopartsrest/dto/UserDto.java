package com.example.autopartsrest.dto;

import com.example.autopartscommon.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String name;
    private String surname;
    private String email;
    private int phone;
    private Role role;
}
