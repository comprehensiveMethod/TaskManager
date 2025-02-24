package com.TaskManager.repositories;

import com.TaskManager.models.Task;
import com.TaskManager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByAuthor(User author);
    List<Task> findByAssignee(User assignee);
}
