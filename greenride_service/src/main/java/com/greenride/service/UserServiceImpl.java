package com.greenride.service;

import com.greenride.dto.RegisterRequest;
import com.greenride.model.Role;
import com.greenride.model.User;
import com.greenride.repository.RoleRepository;
import com.greenride.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Using Constructor Injection (Best Practice from Office Hours)
    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                           RoleRepository roleRepository, 
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        if (registerRequest == null) throw new NullPointerException("Register request cannot be null");

        // 1. Sanitization (Strip whitespaces)
        String username = registerRequest.username().strip();
        String email = registerRequest.email().strip();
        String password = registerRequest.password();

        // 2. Business Logic Validation (Domain Check)
        // Mimicking the @hua.gr check from Office Hours
        // You can remove this or change it to "@greenride.com" if you want internal users only
        /* if (!email.endsWith("@greenride.com")) {
             throw new RuntimeException("Error: Only @greenride.com emails are allowed.");
        }
        */

        // 3. Robust Uniqueness Checks
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // 4. Creation
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(Set.of(userRole));
        user.setEnabled(true);

        return userRepository.save(user);
    }
}
