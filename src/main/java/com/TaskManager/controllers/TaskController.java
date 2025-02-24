package com.TaskManager.controllers;

import com.TaskManager.dtos.TaskRequestDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.models.Task;
import com.TaskManager.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @PreAuthorize("hasRole('ADMIN') or @taskService.isTaskOwner(#id, authentication.principal)")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequestDto));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }

    }

    @PreAuthorize("hasRole('ADMIN') or @taskService.isTaskOwner(#id, authentication.principal)")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto taskRequestDto) {
        try {
            return ResponseEntity.ok(taskService.updateTask(id, taskRequestDto));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PreAuthorize("hasRole('ADMIN') or @taskService.isTaskOwner(#id, authentication.principal)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }

    }
}
