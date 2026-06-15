package com.senhorcafe.urlshortner.user.controller;

import com.senhorcafe.urlshortner.user.dto.AuthResponse;
import com.senhorcafe.urlshortner.user.dto.SignInRequest;
import com.senhorcafe.urlshortner.user.dto.SignUpRequest;
import com.senhorcafe.urlshortner.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signIn")
    public AuthResponse signIn(@RequestBody SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

    @PostMapping("/signUp")
    public AuthResponse signUp(@RequestBody SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }
}
