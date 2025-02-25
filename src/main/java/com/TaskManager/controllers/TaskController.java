package com.TaskManager.controllers;

import com.TaskManager.dtos.TaskRequestDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.models.TaskStatus;
import com.TaskManager.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "Управление задачами")
public class TaskController {
    private final TaskService taskService;
    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по её идентификатору")
    @ApiResponse(responseCode = "200", description = "Задача найдена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен",content = @Content())
    public ResponseEntity<TaskResponseDto> getTaskById(@Parameter(description = "ID задачи", required = true) @PathVariable Long id){
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Получить все задачи", description = "Возвращает все задачи(включена фильтрация и пагинация)")
    @ApiResponse(responseCode = "200", description = "Задачи найдены")
    @ApiResponse(responseCode = "404", description = "Задачи не найдены",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен",content = @Content())
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(required = false) String status,
                                                             @RequestParam(required = false) String priority){
        return ResponseEntity.ok(taskService.getAllTasks(PageRequest.of(page,size),status,priority));
    }

    @GetMapping("/author/{email}")
    @Operation(summary = "Получить задачи автора(по его email'у)", description = "Возвращает все задачи автора")
    @ApiResponse(responseCode = "200", description = "Задачи найдена")
    @ApiResponse(responseCode = "404", description = "Задачи не найдена",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен",content = @Content())
    public ResponseEntity<List<TaskResponseDto>> getTasksByAuthor(@Parameter(description = "Email автора", required = true)
                                                                      @PathVariable String email){
        try {
            return ResponseEntity.ok(taskService.getTasksByAuthor(email));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/assignee/{email}")
    @Operation(summary = "Получить задачи исполнителя", description = "Возвращает все задачи исполнителя(по его email'у)")
    @ApiResponse(responseCode = "200", description = "Задачи найдена")
    @ApiResponse(responseCode = "404", description = "Задачи не найдены",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен",content = @Content())
    public ResponseEntity<List<TaskResponseDto>> getTasksByAssignee(@Parameter(description = "Email исполнителя", required = true)
                                                                        @PathVariable String email){
        try {
            return ResponseEntity.ok(taskService.getTasksByAssignee(email));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Создать задачу", description = "Создает задачу ")
    @ApiResponse(responseCode = "200", description = "Задачи найдена")
    @ApiResponse(responseCode = "404", description = "Задачи не найдены",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен" ,content = @Content())
    public ResponseEntity<TaskResponseDto> createTask(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные для создания задачи",
            required = true,
            content = @Content(schema = @Schema(implementation = TaskRequestDto.class)))
                                                          @RequestBody TaskRequestDto taskRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequestDto));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    @Operation(summary = "Редактировать задачу", description = "Админ запрос. Позволяет полностью менять содержимое задачи")
    @ApiResponse(responseCode = "200", description = "Задачи изменена")
    @ApiResponse(responseCode = "404", description = "Задача/автор/исполнитель не найдены",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен" ,content = @Content())
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto taskRequestDto) {
        try {
            return ResponseEntity.ok(taskService.updateTask(id, taskRequestDto));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("@taskService.isTaskAssignee(#id, authentication.principal)")
    @PutMapping("/{id}")
    @Operation(summary = "Редактировать задачу", description = "Позволяет менять статус задачи")
    @ApiResponse(responseCode = "200", description = "Задачи изменена")
    @ApiResponse(responseCode = "404", description = "Задача/автор/исполнитель не найдены",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен" ,content = @Content())
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody TaskStatus taskStatus){
        try {
            return ResponseEntity.ok(taskService.updateTask(id, taskStatus));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    @Operation(summary = "Удалить задачу", description = "Админ запрос. Позволяет полностью удалить содержимое задачи")
    @ApiResponse(responseCode = "200", description = "Задача удалена")
    @ApiResponse(responseCode = "404", description = "Задача не найдена",content = @Content())
    @ApiResponse(responseCode = "403", description = "Доступ запрещен" ,content = @Content())
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }

    }
}
