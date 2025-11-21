package com.greenride.controller.api;

import com.greenride.dto.JwtResponse;
import com.greenride.dto.LoginRequest;
import com.greenride.dto.RegisterRequest;
import com.greenride.model.User;
import com.greenride.security.JwtUtils;
import com.greenride.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Authentication-related endpoints (register, login).
 * This controller handles public-facing API endpoints for getting into the system.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for User Registration and Login")
public class ApiAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Handles the HTTP POST request to /api/auth/register.
     * Validates the registration request and uses UserService to create a new user.
     *
     * @param registerRequest DTO containing username, email, and password.
     * @return A ResponseEntity indicating success (201 Created) or failure (400 Bad Request).
     */
    @Operation(summary = "Register a new user", description = "Creates a new user account with the role ROLE_USER.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Username or Email already in use")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Attempt to register the user
            User newUser = userService.registerUser(registerRequest);

            // On success, return 201 Created with a success message
            return ResponseEntity.status(201).body("User registered successfully! User ID: " + newUser.getId());

        } catch (RuntimeException e) {
            // If the service threw an exception (e.g., username taken), return 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Handles the HTTP POST request to /api/auth/login.
     * Authenticates the user and returns a JWT token.
     *
     * @param loginRequest DTO containing username and password.
     * @return A ResponseEntity containing the JWT token and user details.
     */
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or Login Failed")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 1. Attempt Authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            // 2. Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Generate the JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);

            // 4. Get User Details to include in the response
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // 5. Return the JWT and user info
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    null, // You can fetch the actual ID if you modify UserDetails to hold it
                    userDetails.getUsername(),
                    null, // Email is not in standard UserDetails
                    roles
            ));

        } catch (BadCredentialsException e) {
            // Explicitly catch "Bad Credentials" (Wrong password/username)
            return ResponseEntity.status(401).body("Error: Invalid username or password");
        } catch (Exception e) {
            // Catch any other errors (like generic AuthenticationException)
            return ResponseEntity.status(401).body("Error: Authentication failed. " + e.getMessage());
        }
    }
}