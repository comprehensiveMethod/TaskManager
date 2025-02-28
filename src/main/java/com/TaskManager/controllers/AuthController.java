package com.TaskManager.controllers;

import com.TaskManager.dtos.JwtRequest;
import com.TaskManager.dtos.JwtResponse;
import com.TaskManager.dtos.RegistrationUserDto;
import com.TaskManager.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@AllArgsConstructor
@Tag(name = "Authentication", description = "Управление аунтефикацией и регистрацией пользователей")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    @Operation(summary = "Авторизация пользователя", description = "Авторизирует пользователя и выдает JWT токен на 2 часа")
    @ApiResponse(responseCode = "200", description = "Пользователь авторизирован", content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @ApiResponse(responseCode = "401", description = "Ошибка авторизации",content = @Content())
    @ApiResponse(responseCode = "400", description = "Неправильные данные",content = @Content())
    public ResponseEntity<?> createAuthToken(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для авторизации пользователя",
            required = true,
            content = @Content(schema = @Schema(implementation = JwtRequest.class)))
            @RequestBody JwtRequest authRequest){
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    @Operation(summary = "Зарегистрировать пользователя", description = "Создает пользователя с указанной почтой и паролем")
    @ApiResponse(responseCode = "200", description = "Пользователь создан", content = @Content(schema = @Schema(implementation = RegistrationUserDto.class)))
    @ApiResponse(responseCode = "401", description = "Ошибка регистрации",content = @Content())
    @ApiResponse(responseCode = "400", description = "Неправильные данные",content = @Content())
    public ResponseEntity<?> createUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для регистрации пользователя",
            required = true,
            content = @Content(schema = @Schema(implementation = RegistrationUserDto.class)))
                                            @RequestBody RegistrationUserDto registrationUserDto) {
        return authService.createUser(registrationUserDto);
    }
}
