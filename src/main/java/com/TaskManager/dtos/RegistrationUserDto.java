package com.TaskManager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegistrationUserDto {

    @Schema(
            description = "Email пользователя",
            example = "ExampleUser@gmail.com"
    )
    private String email;
    @Schema(
            description = "Пароль пользователя",
            example = "123qwerty"
    )
    private String password;
    @Schema(
            description = "Подтверждение пароля пользователя",
            example = "123qwerty"
    )
    private String confirmPassword;
}
