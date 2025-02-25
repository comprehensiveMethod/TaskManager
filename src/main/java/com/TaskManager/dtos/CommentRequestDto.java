package com.TaskManager.dtos;

import lombok.Data;

@Data
public class CommentRequestDto {

    //TODO описать для API
    private String text;

    private Long task_id;

    private String authorEmail;
}
