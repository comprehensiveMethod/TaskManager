package com.TaskManager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    @Schema(
            description = "Токен пользователя",
            example = "t000kensv4444lue.eyJyb2xlcyI6WyJBRE1JTiJdLCJlbWFpbCI6ImV4YW1wbGVAbWFpbC5ydSIsInN1YiI6ImV4YW1wbGVAbWFpbC5ydSIsImlhdCI6MTc0MDU1OTE1OCwiZXhwIjoxNzQwNTY2MzU4fQ.MeqbYEZ_IjhEcou1cUjePXlDOuXbIm8MZk7z9d6iltE"
    )
    private String token;
}
