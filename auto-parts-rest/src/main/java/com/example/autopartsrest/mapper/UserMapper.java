package com.example.autopartsrest.mapper;

import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.CreateUserRequestDto;
import com.example.autopartsrest.dto.UpdateUserDto;
import com.example.autopartsrest.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(CreateUserRequestDto dto);

    UserDto mapToDto(User entity);

    UserDto matToUpdate(UpdateUserDto updateUserDto);
}
