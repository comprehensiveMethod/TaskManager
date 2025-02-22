package com.TaskManager;

import com.TaskManager.models.Task;
import com.TaskManager.models.TaskPriority;
import com.TaskManager.models.TaskStatus;
import com.TaskManager.services.TaskService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}
}
