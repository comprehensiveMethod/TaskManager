package com.TaskManager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    @Schema(
            description = "Id комментария",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Текст комментария",
            example = "Просмотрел 20 deprecated методов и их замен в SpringSecurity"
    )
    private String text;

    @Schema(
            description = "Id задачи",
            example = "1"
    )
    private Long taskId;

    @Schema(
            description = "Email автора комментария",
            example = "Commenter@example.com"
    )
    private String authorEmail;
}
