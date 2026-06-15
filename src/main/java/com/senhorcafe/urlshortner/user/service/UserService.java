package com.senhorcafe.urlshortner.user.service;

import com.senhorcafe.urlshortner.user.dto.AuthResponse;
import com.senhorcafe.urlshortner.user.dto.SignInRequest;
import com.senhorcafe.urlshortner.user.dto.SignUpRequest;
import com.senhorcafe.urlshortner.user.entity.User;
import com.senhorcafe.urlshortner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        return user;
    }

    public AuthResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmail(signInRequest.email());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }
        return new AuthResponse(jwtService.generateToken(user));
    }

    public AuthResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.email()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        User user = new User();
        user.setEmail(signUpRequest.email());
        user.setPassword(passwordEncoder.encode(signUpRequest.password()));
        user = userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user));
    }
}
