package com.mipt.aleksandrivanovich.second_sem.hw_1.api;

import com.mipt.aleksandrivanovich.second_sem.hw_1.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public AuthController(UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("token", token));
    }

    public record LoginRequest(String username, String password) {
    }
}