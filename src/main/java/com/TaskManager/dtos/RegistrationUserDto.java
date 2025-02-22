package com.TaskManager.dtos;

import lombok.Data;

@Data
public class RegistrationUserDto {
    private String email;
    private String password;
    private String confirmPassword;
}
