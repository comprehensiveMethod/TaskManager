package com.TaskManager.dtos;

import com.TaskManager.models.Comment;
import com.TaskManager.models.TaskPriority;
import com.TaskManager.models.TaskStatus;
import com.TaskManager.models.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class TaskRequestDto {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private String author_email;

    private String assignee_email;

}
