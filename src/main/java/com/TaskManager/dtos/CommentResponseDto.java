package com.TaskManager.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    //TODO описать для API
    private Long id;

    private String text;

    private Long task_id;

    private String authorEmail;
}
