package com.TaskManager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentRequestDto {

    @Schema(
            description = "Текст комментария",
            example = "Настроил новый SecurityFilterChain(не понравилось)"
    )
    private String text;

    @Schema(
            description = "Id задачи",
            example = "1"
    )
    private Long taskId;

}
