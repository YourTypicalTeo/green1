package com.greenride.service;

import com.greenride.dto.RegisterRequest;
import com.greenride.model.Role;
import com.greenride.model.User;
import com.greenride.repository.RoleRepository;
import com.greenride.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        if (registerRequest == null) throw new IllegalArgumentException("Request cannot be null");

        // ROBUSTNESS: Strip whitespace (from Office Hours pattern)
        String username = registerRequest.username().strip();
        String email = registerRequest.email().strip();
        String password = registerRequest.password();

        // Optional: Enforce Domain Check (like Office Hours check for @hua.gr)
        // if (!email.endsWith("@greenride.com")) {
        //    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only @greenride.com emails allowed.");
        // }

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Email is already in use!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error: Role not initialized."));

        user.setRoles(Set.of(userRole));
        user.setEnabled(true);

        return userRepository.save(user);
    }
}