package com.example.autopartsweb.service.impl;

import com.example.autopartscommon.entity.Role;
import com.example.autopartscommon.entity.User;
import com.example.autopartscommon.repository.UserRepository;
import com.example.autopartsweb.service.MailService;
import com.example.autopartsweb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    @Value("${site.url}")
    private String siteUrl;


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        return byId;
    }

    @Override
    public void verifyUser(String email, String token) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            if (!byEmail.get().getEnabled()) {
                if (byEmail.get().getToken().equals(token)) {
                    User user = byEmail.get();
                    user.setEnabled(true);
                    user.setToken(null);
                    userRepository.save(user);
                }
            }
        }
    }


    @Override
    public User save(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isEmpty()) {
            String password = user.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            user.setPassword(encodePassword);
            user.setRole(Role.USER);
            UUID token = UUID.randomUUID();
            user.setEnabled(false);
            user.setToken(token.toString());
            userRepository.save(user);
            log.info("User with {} email registered", user.getEmail());
            mailService.sendMail(user.getEmail(), "Welcome",
                    "Hi " + user.getName() +
                            "Welcome to our site!!!" + siteUrl +
                            "/user/verify?email=" + user.getEmail() +
                            "&token=" + token
            );
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}
