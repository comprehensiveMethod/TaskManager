package com.TaskManager;

import com.TaskManager.dtos.TaskRequestDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.models.TaskPriority;
import com.TaskManager.models.TaskStatus;
import org.springframework.boot.test.context.SpringBootTest;


import com.TaskManager.models.Task;
import com.TaskManager.repositories.TaskRepository;
import com.TaskManager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class TaskServiceTests {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.HIGH);
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setDescription(task.getDescription());
        taskRequestDto.setTitle(task.getTitle());
        taskRequestDto.setStatus(task.getStatus());
        taskRequestDto.setPriority(task.getPriority());
        when(taskRepository.save(task)).thenReturn(task);
        TaskResponseDto createdTask = taskService.createTask(taskRequestDto);
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }


}
