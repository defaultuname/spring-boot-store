package com.defuname.springbootstoreex.service;

import com.defuname.springbootstoreex.domain.Role;
import com.defuname.springbootstoreex.domain.Status;
import com.defuname.springbootstoreex.domain.User;
import com.defuname.springbootstoreex.exception.UserAlreadyExistsException;
import com.defuname.springbootstoreex.exception.UserNotFoundException;
import com.defuname.springbootstoreex.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById() {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.getUserById(1L);
        assertDoesNotThrow(() -> new UserNotFoundException(1L));

        when(userRepository.findById(2L)).thenThrow(new UserNotFoundException(2L));
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void getAllUsers() {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);
        List<User> found = userService.getAllUsers();
        verify(userRepository).findAll();

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(3);
        assertThat(found.get(0)).isEqualTo(user);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void getLoggedUser() {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        userService.getLoggedUser();
        assertDoesNotThrow(() -> new UsernameNotFoundException("test@mail.com"));
    }

    @Test
    void saveUser() {
        User user1 = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);
        User user2 = new User("user@mail.com", "user", "user", "12345", Role.USER, Status.ACTIVE);

        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user1));

        assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(user1)); // Сохранение существующего юзера
        userService.saveUser(user2); // Сохранение несуществующего юзера
    }

    @Test
    void editUser() {
        User user = new User("test@mail.com", "admin", "admin", "12345", Role.USER, Status.ACTIVE);

        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.editUser(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User found = userService.getUserById(1L);

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(user);
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(1L);
        verify(userRepository).deleteById(1L);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }
}