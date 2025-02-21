package com.TaskManager.repositories;

import com.TaskManager.models.Comment;
import com.TaskManager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}