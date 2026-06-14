package com.mipt.aleksandrivanovich.second_sem.hw_1.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return switch (username) {
            case "user" -> new User(
                "user",
                passwordEncoder.encode("password"),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            case "reader" -> new User(
                "reader",
                passwordEncoder.encode("password"),
                List.of(
                    new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("READ_PRIVILEGE")
                )
            );
            default -> throw new UsernameNotFoundException("User not found: " + username);
        };
    }
}