package com.TaskManager.taskTests;

import com.TaskManager.dtos.TaskAdminRequestDto;
import com.TaskManager.dtos.TaskRequestDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.models.Task;
import com.TaskManager.models.TaskPriority;
import com.TaskManager.models.TaskStatus;
import com.TaskManager.models.User;
import com.TaskManager.repositories.TaskRepository;
import com.TaskManager.repositories.UserRepository;
import com.TaskManager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class TaskServiceTests {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User author;
    private User assignee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new User();
        author.setEmail("author@example.com");
        author.setId(1L);
        assignee = new User();
        assignee.setEmail("assignee@example.com");
        assignee.setId(1L);
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Task Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.HIGH);
        task.setAuthor(author);
        task.setAssignee(assignee);
    }

    @Test
    void createTask_ShouldReturnTaskResponseDto() {
        TaskRequestDto requestDto = new TaskRequestDto();
        requestDto.setPriority(TaskPriority.HIGH);
        requestDto.setDescription("Task Description");
        requestDto.setTitle("Test Task");
        requestDto.setAssigneeEmail("assignee@example.com");
        requestDto.setStatus(TaskStatus.IN_PROGRESS);

        when(userRepository.findByEmail("assignee@example.com")).thenReturn(Optional.of(assignee));
        when(userRepository.findByEmail("author@example.com")).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto responseDto = taskService.createTask(requestDto, "author@example.com");

        assertNotNull(responseDto);
        assertEquals("Test Task", responseDto.getTitle());
        assertEquals("author@example.com", responseDto.getAuthorEmail());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldReturnUpdatedTaskResponseDto() {
        TaskAdminRequestDto updateDto = new TaskAdminRequestDto();
        updateDto.setPriority(TaskPriority.HIGH);
        updateDto.setDescription("Updated Task");
        updateDto.setTitle("Updated Task");
        updateDto.setAssigneeEmail("assignee@example.com");
        updateDto.setStatus(TaskStatus.IN_PROGRESS);
        updateDto.setAuthorEmail("author@example.com");
        System.out.println(task.toString());
        when(userRepository.findByEmail("assignee@example.com")).thenReturn(Optional.of(assignee));
        when(userRepository.findByEmail("author@example.com")).thenReturn(Optional.of(author));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        TaskResponseDto responseDto = taskService.updateTask(1L, updateDto);

        assertNotNull(responseDto);
        assertEquals("Updated Task", responseDto.getTitle());
        assertEquals("author@example.com", responseDto.getAuthorEmail());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTaskStatus_ShouldReturnUpdatedTaskResponseDto() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto responseDto = taskService.updateTask(1L, TaskStatus.IN_PROGRESS);

        assertNotNull(responseDto);
        assertEquals(TaskStatus.IN_PROGRESS, responseDto.getStatus());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTaskById_ShouldReturnTaskResponseDto() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDto responseDto = taskService.getTaskById(1L);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTasksByAuthor_ShouldReturnListOfTasks() {
        when(userRepository.findByEmail("author@example.com")).thenReturn(Optional.of(author));
        when(taskRepository.findByAuthor(author)).thenReturn(List.of(task));

        List<TaskResponseDto> tasks = taskService.getTasksByAuthor("author@example.com");

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());

        verify(taskRepository, times(1)).findByAuthor(author);
    }

    @Test
    void getTasksByAssignee_ShouldReturnListOfTasks() {
        when(userRepository.findByEmail("assignee@example.com")).thenReturn(Optional.of(assignee));
        when(taskRepository.findByAssignee(assignee)).thenReturn(List.of(task));

        List<TaskResponseDto> tasks = taskService.getTasksByAssignee("assignee@example.com");

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());

        verify(taskRepository, times(1)).findByAssignee(assignee);
    }

    @Test
    void deleteTask_ShouldCallRepositoryDeleteById() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void isTaskAssignee_ShouldReturnTrue_WhenEmailMatches() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        boolean result = taskService.isTaskAssignee(1L, "assignee@example.com");

        assertTrue(result);

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getAllTasks_ShouldReturnPagedTasks() {
        Pageable pageable = mock(Pageable.class);
        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<TaskResponseDto> responsePage = taskService.getAllTasks(pageable, null, null);

        assertFalse(responsePage.isEmpty());
        assertEquals(1, responsePage.getTotalElements());

        verify(taskRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}
