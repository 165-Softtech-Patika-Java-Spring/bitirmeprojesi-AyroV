package com.softtech.webapp.security.service;

import com.softtech.webapp.security.dto.LoginRequestDto;
import com.softtech.webapp.security.enums.EnumJwtConstant;
import com.softtech.webapp.security.jwt.JwtTokenGenerator;
import com.softtech.webapp.security.jwt.JwtUserDetails;
import com.softtech.webapp.user.dto.UserGetDto;
import com.softtech.webapp.user.dto.UserPostDto;
import com.softtech.webapp.user.entity.User;
import com.softtech.webapp.user.service.UserEntityService;
import com.softtech.webapp.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final UserEntityService userEntityService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;

    public UserGetDto register(UserPostDto userPostDto) {
        return userService.save(userPostDto);
    }

    public String login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(), loginRequestDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenGenerator.generateJwtToken(authentication);

        String bearer = EnumJwtConstant.BEARER.getConstant();

        return bearer + token;
    }

    public User getCurrentUser() {
        JwtUserDetails jwtUserDetails = getCurrentJwtUserDetails();

        User user = null;
        if (jwtUserDetails != null){
            user = userEntityService.getByIdWithControl(jwtUserDetails.getId());
        }

        return user;
    }

    public Long getCurrentUserId() {
        JwtUserDetails jwtUserDetails = getCurrentJwtUserDetails();
        return jwtUserDetails.getId();
    }

    private JwtUserDetails getCurrentJwtUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtUserDetails jwtUserDetails = null;
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails){
            jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        }
        return jwtUserDetails;
    }
}
