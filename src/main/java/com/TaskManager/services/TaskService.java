package com.TaskManager.services;

import com.TaskManager.dtos.TaskRequestDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.models.Task;
import com.TaskManager.repositories.TaskRepository;
import com.TaskManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task task = new Task();
        task.setAssignee(userRepository.findByEmail(taskRequestDto.getAssignee_email()).orElseThrow(() -> new NullPointerException("Assignee not found")));
        task.setAuthor(userRepository.findByEmail(taskRequestDto.getAuthor_email()).orElseThrow(() -> new NullPointerException("Author not found")));
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());
        task.setTitle(taskRequestDto.getTitle());
        Task savedTask = taskRepository.save(task);
        return new TaskResponseDto(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getPriority(),
                savedTask.getAuthor().getEmail(),
                savedTask.getAssignee().getEmail()
        );
    }

    public TaskResponseDto updateTask(Long id, TaskRequestDto taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NullPointerException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());
        task.setAssignee(userRepository.findByEmail(taskDetails.getAssignee_email()).orElseThrow(() -> new NullPointerException("Assignee not found")));
        task.setAuthor(userRepository.findByEmail(taskDetails.getAuthor_email()).orElseThrow(() -> new NullPointerException("Author not found")));
        Task savedTask = taskRepository.save(task);
        return new TaskResponseDto(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getPriority(),
                savedTask.getAuthor().getEmail(),
                savedTask.getAssignee().getEmail()
        );
    }
    public TaskResponseDto getTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new NullPointerException("Task not found"));
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAuthor().getEmail(),
                task.getAssignee().getEmail()
        );
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public boolean isTaskOwner(Long taskId, String email) {
        return taskRepository.findById(taskId).get().getAuthor().getEmail().equals(email);
    }
}