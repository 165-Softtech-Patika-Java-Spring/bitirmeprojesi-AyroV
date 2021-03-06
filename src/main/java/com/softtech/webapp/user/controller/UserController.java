package com.softtech.webapp.user.controller;

import com.softtech.webapp.user.dto.*;
import com.softtech.webapp.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(tags = "User Controller")
    @GetMapping
    public ResponseEntity<List<UserGetDto>> findAll(@RequestParam(required = false) String username) {
        if (username != null) {
            UserGetDto userGetDto = userService.findByUsername(username);
            List<UserGetDto> userList = new ArrayList<>();
            userList.add(userGetDto);
            return ResponseEntity.ok().body(userList);
        }

        List<UserGetDto> userGetDtoList = userService.findAll();
        return ResponseEntity.ok().body(userGetDtoList);
    }

    @Operation(tags = "User Controller")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        UserGetDto userGetDto = userService.findById(id);
        return ResponseEntity.ok().body(userGetDto);
    }

    @Operation(tags = "User Controller")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid UserPostDto userPostDto) {
        UserGetDto userGetDto = userService.save(userPostDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userGetDto);
    }

    @Operation(tags = "User Controller")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestBody @Valid UserDeleteDto userDeleteDto) {
        userService.delete(userDeleteDto, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(tags = "User Controller")
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserPatchDto userPatchDto) {
        UserGetDto userGetDto = userService.update(userPatchDto, id);
        return ResponseEntity.ok().body(userGetDto);
    }
}
