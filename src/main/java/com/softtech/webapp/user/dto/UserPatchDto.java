package com.softtech.webapp.user.dto;

import lombok.Data;

@Data
public class UserPatchDto {
    private String username;
    private String name;
    private String surname;
}