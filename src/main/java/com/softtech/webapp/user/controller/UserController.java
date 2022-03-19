package com.softtech.webapp.user.controller;

import com.softtech.webapp.user.dto.*;
import com.softtech.webapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) String username) {
        if (username != null) {
            UserGetDto userGetDto = userService.findByUsername(username);
            return ResponseEntity.ok().body(userGetDto);
        }

        List<UserGetDto> userGetDtoList = userService.findAll();
        return ResponseEntity.ok().body(userGetDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        UserGetDto userGetDto = userService.findById(id);
        return ResponseEntity.ok().body(userGetDto);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid UserPostDto userPostDto) {
        UserGetDto userGetDto = userService.save(userPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userGetDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody @Valid UserDeleteDto userDeleteDto) {
        userService.delete(userDeleteDto, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserPatchDto userPatchDto) {
        UserGetDto userGetDto = userService.update(userPatchDto, id);
        return ResponseEntity.ok().body(userGetDto);
    }
}
