package com.TaskManager.dtos;

import com.TaskManager.models.Task;
import com.TaskManager.models.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CommentRequestDto {


    private String text;

    private Long task_id;

    private String authorEmail;
}
