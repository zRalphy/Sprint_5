package org.openapitools.controller;

import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.UUID;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.CreateReminderRequest;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.openapitools.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${openapi.todo.base-path:}")
public class TaskApiController implements TaskApi {
	private final TaskService taskService;

	@Autowired
	public TaskApiController(TaskService taskService) {
		this.taskService = taskService;
	}

	@Override
	public ResponseEntity<TaskResponse> getTask(Long taskId) throws ApiException {
		return new ResponseEntity<>(taskService.getTaskByUserIdAndTaskId(taskId), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<TaskResponse>> listTasks() {
		return new ResponseEntity<>(taskService.getAllTasksByUserId(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<TaskResponse> createTask(TaskCreateRequest taskCreateRequest) throws ApiException {
		return new ResponseEntity<>(taskService.createTask(taskCreateRequest), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<TaskResponse> updateTask(Long taskId, TaskUpdateRequest taskUpdateRequest) throws ApiException {
		return new ResponseEntity<>(taskService.updateTask(taskId, taskUpdateRequest), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> deleteTask(Long taskId) throws ApiException {
		taskService.deleteTask(taskId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@RolesAllowed(OAUTH_USER)
	public ResponseEntity<TaskResponse> createTaskReminder(UUID taskId, CreateReminderRequest reminderRequest) throws ApiException {
		return new ResponseEntity<>(
				taskService.createTaskReminder(taskId, reminderRequest), HttpStatus.OK);
	}
}
