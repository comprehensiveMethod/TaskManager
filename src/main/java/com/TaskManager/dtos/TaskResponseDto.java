package com.TaskManager.dtos;

import com.TaskManager.models.TaskPriority;
import com.TaskManager.models.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponseDto {
    @Schema(
            description = "Уникальный идентификатор задачи",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Название задачи",
            example = "Рефакторинг кода" // Пример значения
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
            description = "Email автора задачи",
            example = "exampleAuthor@mail.ru"
    )
    private String author_email;

    @Schema(
            description = "Email исполнителя задачи",
            example = "exampleAssignee@mail.ru"
    )
    private String assignee_email;

}
