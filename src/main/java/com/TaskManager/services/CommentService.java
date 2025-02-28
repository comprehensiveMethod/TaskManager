package com.TaskManager.services;

import com.TaskManager.dtos.CommentRequestDto;
import com.TaskManager.dtos.CommentResponseDto;
import com.TaskManager.models.Task;
import com.TaskManager.models.User;
import com.TaskManager.repositories.CommentRepository;
import com.TaskManager.repositories.TaskRepository;
import com.TaskManager.repositories.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    private final TaskService taskService;
    //создаем коммент
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto,String requester_email) {
        Task task = taskRepository.findById(commentRequestDto.getTaskId()).orElseThrow(() -> new NullPointerException(
                "Task not found"
        ));
        if(!taskService.isTaskAssignee(task.getId(),requester_email)){
            throw new IllegalStateException("Cant comment if not assignee");
        }
        Comment comment = new Comment();
        comment.setAuthor(userRepository.findByEmail(requester_email).orElseThrow(() -> new NullPointerException(
                "User not found"
        )));
        comment.setTask(taskRepository.findById(commentRequestDto.getTaskId()).orElseThrow(() -> new NullPointerException(
                "Task not found"
        )));
        comment.setText(commentRequestDto.getText());
        Comment saved_comment = commentRepository.save(comment);
        return toDto(saved_comment);
    }
    //получить коммент по айди
    public CommentResponseDto getCommentById(Long id) {
        Comment saved_comment = commentRepository.findById(id).orElseThrow(() -> new NullPointerException(
                "Comment not found"
        ));
        return toDto(saved_comment);
    }
    //получить все комменты с фильтром на автора и айди таски
    public Page<CommentResponseDto> getAllTasks(Pageable pageable, String authorEmail, Long taskId) {
        Specification<Comment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (authorEmail != null) {
                User user = userRepository.findByEmail(authorEmail).get();
                predicates.add(cb.equal(root.get("author"), user));
            }
            if (taskId != null) {
                predicates.add(cb.equal(root.get("taskId"), taskRepository.findById(taskId).get()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Comment> comments = commentRepository.findAll(spec, pageable);
        return comments.map(this::toDto);
    }
    //Comment to CommentResponseDto
    public CommentResponseDto toDto(Comment comment){
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getTask().getId(),
                comment.getAuthor().getEmail()

        );
    }
}
