package com.example.autopartsrest.endpoint;

import com.example.autopartscommon.entity.User;
import com.example.autopartsrest.dto.*;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import com.example.autopartsrest.mapper.UserMapper;
import com.example.autopartsrest.security.CurrentUser;
import com.example.autopartsrest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDto> auth(@RequestBody UserAuthRequestDto userAuthRequestDto) throws UserUnauthorizedException {
        String auth = userService.auth(userAuthRequestDto);
        return ResponseEntity.ok(new UserAuthResponseDto(auth));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody CreateUserRequestDto createUserRequestDto) throws EntityNotFoundException {
        User user = userService.registerUser(createUserRequestDto);
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getAllById(@PathVariable("id") int id) throws EntityNotFoundException {
        return ResponseEntity.ok(userMapper.mapToDto(userService.getAllById(id)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) throws EntityNotFoundException {
        userService.deleteById(id);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") int id, @RequestBody UpdateUserDto updateUserDto,
                                          @AuthenticationPrincipal CurrentUser currentUser) throws UserUnauthorizedException {

        return ResponseEntity.ok(userService.updateUser(id, updateUserDto, currentUser.getUser()));
    }
}