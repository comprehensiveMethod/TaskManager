package com.TaskManager.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;

    private String text;

    private Long task_id;

    private String authorEmail;
}
