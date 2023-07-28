package com.example.autopartsrest.service.impl;

import com.example.autopartscommon.entity.Role;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.UserRepository;
import com.example.autopartsrest.dto.CreateUserRequestDto;
import com.example.autopartsrest.dto.UpdateUserDto;
import com.example.autopartsrest.dto.UserAuthRequestDto;
import com.example.autopartsrest.dto.UserDto;
import com.example.autopartsrest.exception.EntityNotFoundException;
import com.example.autopartsrest.exception.UserUnauthorizedException;
import com.example.autopartsrest.mapper.UserMapper;
import com.example.autopartsrest.service.UserService;
import com.example.autopartsrest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }


    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(int id) throws EntityNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User whit " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User getAllById(int id) throws EntityNotFoundException {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("User whit " + id + " not found");
        }
        return byId.get();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(CreateUserRequestDto createUserRequestDto) throws EntityNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(createUserRequestDto.getEmail());
        if (byEmail.isPresent()) {
            throw new EntityNotFoundException("User whit " + byEmail.get().getEmail() + " not found");
        }
        User user = userMapper.map(createUserRequestDto);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        user.setToken(null);
        user.setEnabled(true);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    @Override
    public String auth(UserAuthRequestDto userAuthRequestDto) throws UserUnauthorizedException {
        Optional<User> byEmail = userRepository.findByEmail(userAuthRequestDto.getEmail());
        if (byEmail.isEmpty()) {
            throw new UserUnauthorizedException("User unauthorized");
        }
        User user = byEmail.get();
        if (!passwordEncoder.matches(userAuthRequestDto.getPassword(), user.getPassword())) {
            throw new UserUnauthorizedException("User unauthorized");
        }
        return jwtTokenUtil.generateToken(userAuthRequestDto.getEmail());
    }

    @Override
    public UserDto updateUser(int id, UpdateUserDto updateUserDto, User currentUser) throws UserUnauthorizedException {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new UserUnauthorizedException("User Unauthorized");
        }
        if (byId.get().getId() != currentUser.getId()) {
            throw new UserUnauthorizedException("You can change yours");
        }

        Optional<User> byEmail = userRepository.findByEmail(updateUserDto.getEmail());
        if (byEmail.isPresent() && byEmail.get().getId() != id) {
            throw new UserUnauthorizedException("email " + byEmail.get().getEmail() + " is present");
        }
        User userFromDb = byId.get();
        if (updateUserDto.getName() != null && !updateUserDto.getName().isEmpty()) {
            userFromDb.setName(updateUserDto.getName());
        }
        if (updateUserDto.getSurname() != null && !updateUserDto.getSurname().isEmpty()) {
            userFromDb.setSurname(updateUserDto.getSurname());
        }
        if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().isEmpty()) {
            userFromDb.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getPassword() != null && !updateUserDto.getPassword().isEmpty()) {
            userFromDb.setPassword(updateUserDto.getPassword());
            userFromDb.setPassword(passwordEncoder.encode(userFromDb.getPassword()));
        }
        userFromDb.setRole(Role.USER);
        userFromDb.setEnabled(true);
        userFromDb.setToken(null);
        UserDto userDto = userMapper.matToUpdate(updateUserDto);
        userRepository.save(userFromDb);
        return userDto;
    }

    @Override
    public boolean existsById(int id) {
        return userRepository.existsById(id);
    }
}
