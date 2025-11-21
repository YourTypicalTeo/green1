package com.greenride.service;

import com.greenride.dto.RegisterRequest;
import com.greenride.model.Role;
import com.greenride.model.User;
import com.greenride.repository.RoleRepository;
import com.greenride.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service; // <-- CHECK THIS IMPORT
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service // <-- THIS ANNOTATION IS ESSENTIAL
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(Set.of(userRole));
        user.setEnabled(true);

        return userRepository.save(user);
    }
}