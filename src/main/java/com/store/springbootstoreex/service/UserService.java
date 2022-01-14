package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.Role;
import com.store.springbootstoreex.domain.Status;
import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.exception.UserAlreadyExistsException;
import com.store.springbootstoreex.exception.UserNotFoundException;
import com.store.springbootstoreex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(s -> {
            throw new UserAlreadyExistsException(user.getEmail());
        });

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
