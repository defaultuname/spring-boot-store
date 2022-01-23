package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.exception.UserAlreadyExistsException;
import com.store.springbootstoreex.exception.UserNotFoundException;
import com.store.springbootstoreex.repository.UserRepository;
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

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getLoggedUser() { // Получение залогиненного юзера
        return getUserByEmail(SecurityContextHolder // Получаем из сессии его эл. почту, далее находим юзера по ней
                .getContext().getAuthentication().getName());
    }

    public void saveUser(User user) {
        userRepository.findByEmail(user.getEmail().toLowerCase()).ifPresent(s -> {
            throw new UserAlreadyExistsException(user.getEmail());
        }); // Перед сохранением нового пользователя проверяем, не существует ли уже пользователь с такой эл. почтой

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword())); // И кодируем его пароль через BCrypt
        userRepository.save(user);
    }

    public void editUser(User user) { // Чтобы изменение существующего пользователя стало возможным, в методе убрана проверка на идентичную эл. почту.
        user.setPassword(getUserById(user.getId()).getPassword()); // Пароль изменить невозможно из соображений безопасности. Просто оставляем его прежним.
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
