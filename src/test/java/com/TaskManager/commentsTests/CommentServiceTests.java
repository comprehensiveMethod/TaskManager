package com.TaskManager.commentsTests;

import com.TaskManager.dtos.CommentRequestDto;
import com.TaskManager.dtos.CommentResponseDto;
import com.TaskManager.models.Comment;
import com.TaskManager.models.Task;
import com.TaskManager.models.User;
import com.TaskManager.repositories.CommentRepository;
import com.TaskManager.repositories.TaskRepository;
import com.TaskManager.repositories.UserRepository;
import com.TaskManager.services.CommentService;
import com.TaskManager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTests {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");

        task = new Task();
        task.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setTask(task);
        comment.setAuthor(user);
    }

    @Test
    void createComment_ShouldReturnCommentResponseDto() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setText("Test comment");
        requestDto.setTaskId(1L);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(taskService.isTaskAssignee(1L, "test@example.com")).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto responseDto = commentService.createComment(requestDto, "test@example.com");

        assertNotNull(responseDto);
        assertEquals(comment.getText(), responseDto.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void getCommentById_ShouldReturnCommentResponseDto() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        CommentResponseDto responseDto = commentService.getCommentById(1L);

        assertNotNull(responseDto);
        assertEquals(comment.getId(), responseDto.getId());
        assertEquals(comment.getText(), responseDto.getText());
    }



    @Test
    void getCommentsByTaskId_ShouldReturnListOfComments() {
        when(commentRepository.findByTaskId(1L)).thenReturn(List.of(comment));

        List<CommentResponseDto> response = commentService.getCommentsByTaskId(1L);

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void getCommentsByAuthor_ShouldReturnListOfComments() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(commentRepository.findByAuthorId(user.getId())).thenReturn(List.of(comment));

        List<CommentResponseDto> response = commentService.getCommentsByAuthor("test@example.com");

        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }
}
