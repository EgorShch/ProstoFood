package com.example.Products.services;

import com.example.Products.models.Role;
import com.example.Products.models.User;
import com.example.Products.respositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user){
        if (userRepository.findByLogin(user.getLogin()) != null) return false;
        user.getRoles().add(Role.ROLE_USER);
        user.getRoles().add(Role.ROLE_ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public User getByLogin(String login){
        return userRepository.findByLogin(login);
    }

}
