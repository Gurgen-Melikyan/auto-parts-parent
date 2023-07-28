package com.example.autopartsrest.service;



import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.CreateUserRequestDto;
import com.example.autopartsrest.dto.UpdateUserDto;
import com.example.autopartsrest.dto.UserAuthRequestDto;
import com.example.autopartsrest.dto.UserDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findUsers();
    Optional<User> findById(int id);
    User addUser(User user);
    void deleteById(int id) throws EntityNotFoundException;
    User getAllById(int id) throws EntityNotFoundException;
    Optional<User> findByEmail(String email);
    User registerUser(CreateUserRequestDto createUserRequestDto) throws EntityNotFoundException;
    String auth(UserAuthRequestDto userAuthRequestDto) throws UserUnauthorizedException;
    UserDto updateUser(int id, UpdateUserDto updateUserDto, User currentUser) throws UserUnauthorizedException;
    boolean existsById(int id);
}
