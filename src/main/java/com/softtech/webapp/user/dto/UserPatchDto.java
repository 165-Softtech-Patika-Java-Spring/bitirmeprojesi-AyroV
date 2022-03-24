package com.softtech.webapp.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPatchDto {
    private String username;
    private String name;
    private String surname;
    private String password;
}
