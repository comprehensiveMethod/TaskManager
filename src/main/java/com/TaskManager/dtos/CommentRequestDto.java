package com.TaskManager.dtos;

import lombok.Data;

@Data
public class CommentRequestDto {


    private String text;

    private Long task_id;

    private String authorEmail;
}
