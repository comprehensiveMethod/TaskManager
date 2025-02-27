package com.TaskManager.dtos;


import com.TaskManager.models.TaskPriority;
import com.TaskManager.models.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class TaskRequestDto {

    @Schema(
            description = "Название задачи",
            example = "Рефакторинг кода"
    )
    private String title;

    @Schema(
            description = "Описание задачи",
            example = "Описание проблемы и/или её возможные решения"
    )
    private String description;

    @Schema(
            description = "Статус задачи",
            example = "PENDING"
    )
    private TaskStatus status;

    @Schema(
            description = "Приоритет задачи",
            example = "HIGH"
    )
    private TaskPriority priority;

    @Schema(
            description = "Email исполнителя задачи",
            example = "exampleAssignee@mail.ru"
    )
    private String assigneeEmail;

}
