package com.TaskManager.services;

import com.TaskManager.dtos.CommentRequestDto;
import com.TaskManager.dtos.CommentResponseDto;
import com.TaskManager.dtos.TaskResponseDto;
import com.TaskManager.models.User;
import com.TaskManager.repositories.CommentRepository;
import com.TaskManager.repositories.TaskRepository;
import com.TaskManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.TaskManager.models.Comment;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setAuthor(userRepository.findByEmail(commentRequestDto.getAuthorEmail()).orElseThrow(() -> new NullPointerException(
                "User not found"
        )));
        comment.setTask(taskRepository.findById(commentRequestDto.getTask_id()).orElseThrow(() -> new NullPointerException(
                "Task not found"
        )));
        comment.setText(commentRequestDto.getText());
        Comment saved_comment = commentRepository.save(comment);
        return toDto(saved_comment);
    }
    public CommentResponseDto getCommentById(Long id) {
        Comment saved_comment = commentRepository.findById(id).orElseThrow(() -> new NullPointerException(
                "Comment not found"
        ));
        return toDto(saved_comment);
    }

    public List<CommentResponseDto> getCommentsByTaskId(Long taskId) {
        List<Comment> comments = commentRepository.findByTaskId(taskId);
        List<CommentResponseDto> response = new ArrayList<>();
        return comments.stream().map(this::toDto).toList();
    }
    public List<CommentResponseDto> getCommentsByAuthor(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NullPointerException(
                "Email not found"
        ));
        List<Comment> comments = commentRepository.findByAuthorId(user.getId());
        return comments.stream().map(this::toDto).toList();
    }
    public CommentResponseDto toDto(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getTask().getId(),
                comment.getAuthor().getEmail()

        );
    }
}
