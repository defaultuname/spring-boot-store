package com.store.springbootstoreex.service;

import com.store.springbootstoreex.domain.User;
import com.store.springbootstoreex.exception.UserNotFoundException;
import com.store.springbootstoreex.repository.UserRepository;
import com.store.springbootstoreex.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        return SecurityUser.userConvert(user);
    }

    public UserDetails findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return SecurityUser.userConvert(user);
    }

    public List<UserDetails> getAllUsers() {
        return userRepository.findAll().stream().map(SecurityUser::userConvert).collect(Collectors.toList());
    }

    public void saveUser(User user) {
        userRepository.findByEmail(user.getEmail()).orElse(userRepository.save(user));

//        if (userTemp != null) {
//            return false;
//        }
//        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
