package com.TaskManager.services;

import com.TaskManager.dtos.TaskAdminRequestDto;
import com.TaskManager.dtos.TaskRequestDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.models.Task;
import com.TaskManager.models.TaskStatus;
import com.TaskManager.models.User;
import com.TaskManager.repositories.TaskRepository;
import com.TaskManager.repositories.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    //создаем таску
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto, String requester_email) {
        Task task = new Task();
        task.setAssignee(userRepository.findByEmail(taskRequestDto.getAssigneeEmail()).orElseThrow(() -> new NullPointerException("Assignee not found")));
        task.setAuthor(userRepository.findByEmail(requester_email).orElseThrow(() -> new NullPointerException("Author not found")));
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());
        task.setTitle(taskRequestDto.getTitle());
        Task savedTask = taskRepository.save(task);
        return toDto(savedTask);
    }
    //админское обновление таски
    public TaskResponseDto updateTask(Long id, TaskAdminRequestDto taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new NullPointerException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());
        task.setAssignee(userRepository.findByEmail(taskDetails.getAssigneeEmail()).orElseThrow(() -> new NullPointerException("Assignee not found")));
        task.setAuthor(userRepository.findByEmail(taskDetails.getAuthorEmail()).orElseThrow(() -> new NullPointerException("Author not found")));
        Task savedTask = taskRepository.save(task);
        return toDto(savedTask);
    }
    //USER обновление таски(только статус и приоритет)
    public TaskResponseDto updateTask(Long id, TaskStatus taskStatus){
        Task task = taskRepository.findById(id).orElseThrow(() -> new NullPointerException("Task not found"));
        task.setStatus(taskStatus);
        Task savedTask = taskRepository.save(task);
        return toDto(savedTask);
    }

    //создаем таску
    public TaskResponseDto getTaskById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(() -> new NullPointerException("Task not found"));
        return toDto(task);
    }
    //создаем таску по автору
    public List<TaskResponseDto> getTasksByAuthor(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("User not found"));
        List<Task> tasks = taskRepository.findByAuthor(user);
        return tasks.stream().map(this::toDto).toList();
    }
    //создаем таску по исполнителю
    public List<TaskResponseDto> getTasksByAssignee(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("User not found"));
        List<Task> tasks = taskRepository.findByAssignee(user);

        return tasks.stream().map(this::toDto).toList();
    }
    //админское удаление таски
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    //создаем проверка что мейл принадлежит исполнителю таск айди
    public boolean isTaskAssignee(Long taskId, String email) {
        return taskRepository.findById(taskId).get().getAssignee().getEmail().equals(email);
    }
    //Task to TaskResponseDto
    private TaskResponseDto toDto(Task task) {
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
    //Получить все таски с пагинацией и фильтрацией по статусу и приоритету
    public Page<TaskResponseDto> getAllTasks(Pageable pageable, String status, String priority) {
            Specification<Task> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(this::toDto);
    }
}