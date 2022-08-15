package com.defuname.springbootstoreex.service;

import com.defuname.springbootstoreex.domain.User;
import com.defuname.springbootstoreex.exception.UserAlreadyExistsException;
import com.defuname.springbootstoreex.exception.UserNotFoundException;
import com.defuname.springbootstoreex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getLoggedUser() {
        String loggedUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(loggedUserEmail).orElseThrow(() -> new UsernameNotFoundException(loggedUserEmail));
    }

    public void saveUser(User user) {
        userRepository.findByEmail(user.getEmail().toLowerCase()).ifPresent(s -> {
            throw new UserAlreadyExistsException(user.getEmail());
        });

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public void editUser(User user) { // Чтобы изменение существующего пользователя стало возможным, в методе убрана проверка на идентичную эл. почту.
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
