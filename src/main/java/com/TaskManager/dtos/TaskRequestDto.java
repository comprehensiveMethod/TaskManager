package com.TaskManager.dtos;


import com.TaskManager.models.TaskPriority;
import com.TaskManager.models.TaskStatus;

import lombok.Data;


@Data
public class TaskRequestDto {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private String author_email;

    private String assignee_email;

}
