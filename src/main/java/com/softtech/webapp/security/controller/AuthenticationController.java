package com.softtech.webapp.security.controller;


import com.softtech.webapp.security.dto.LoginRequestDto;
import com.softtech.webapp.security.service.AuthenticationService;
import com.softtech.webapp.user.dto.UserGetDto;
import com.softtech.webapp.user.dto.UserPostDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(tags = "Authentication Controller")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto){

        String token = authenticationService.login(loginRequestDto);

        return ResponseEntity.ok(token);
    }

    @Operation(tags = "Authentication Controller")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserPostDto userPostDto){

        UserGetDto userGetDto = authenticationService.register(userPostDto);

        return ResponseEntity.ok(userGetDto);
    }
}
