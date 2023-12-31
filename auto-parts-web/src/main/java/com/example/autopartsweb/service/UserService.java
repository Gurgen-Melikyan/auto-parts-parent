package com.example.autopartsweb.service;



import com.example.autopartscommon.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findById(int id);
    void verifyUser(String email,String token);
    User save(User user);
    void deleteById(int id);
}
