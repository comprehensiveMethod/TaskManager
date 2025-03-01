package com.TaskManager.repositories;

import com.TaskManager.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findByTaskId(Long taskId);
    List<Comment> findByAuthorId(Long authorId);
}