package com.greenride.controller.api;

// --- CHECK THESE IMPORT STATEMENTS ---
import com.greenride.dto.LoginRequest;
import com.greenride.dto.RegisterRequest;
import com.greenride.model.User;
import com.greenride.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// --- END IMPORTS ---

/**
 * REST Controller for Authentication-related endpoints (register, login).
 * This controller handles public-facing API endpoints for getting into the system.
 */
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    // Inject the UserService to handle registration logic
    @Autowired
    private UserService userService; // <-- This is your Line 25 area

    /**
     * Handles the HTTP POST request to /api/auth/register.
     * Validates the registration request and uses UserService to create a new user.
     *
     * @param registerRequest DTO containing username, email, and password.
     * @return A ResponseEntity indicating success (201 Created) or failure (400 Bad Request).
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Attempt to register the user
            User newUser = userService.registerUser(registerRequest); // <-- This is your Line 41 area

            // On success, return 201 Created with a success message
            return ResponseEntity.status(201).body("User registered successfully! User ID: " + newUser.getId());

        } catch (RuntimeException e) {
            // If the service threw an exception (e.g., username taken), return 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Handles the HTTP POST request to /api/auth/login.
     * (This is the next step to implement).
     *
     * @param loginRequest DTO containing username and password.
     * @return A ResponseEntity (to be implemented) containing the JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // TODO: Implement login logic (Phase 3: Security)

        return ResponseEntity.ok("Login logic to be implemented");
    }
}