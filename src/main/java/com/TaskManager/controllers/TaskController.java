package com.TaskManager.controllers;

import com.TaskManager.dtos.TaskRequestDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.services.TaskService;
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
public class TaskController {
    private final TaskService taskService;
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(taskService.getTaskById(id));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(required = false) String status,
                                                             @RequestParam(required = false) String priority){
        return ResponseEntity.ok(taskService.getAllTasks(PageRequest.of(page,size),status,priority));
    }

    @GetMapping("/author/{email}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAuthor(@PathVariable String email){
        try {
            return ResponseEntity.ok(taskService.getTasksByAuthor(email));
        }catch (NullPointerException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/assignee/{email}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAssignee(@PathVariable String email){
        try {
            return ResponseEntity.ok(taskService.getTasksByAssignee(email));
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
