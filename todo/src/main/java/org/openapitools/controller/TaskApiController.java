package org.openapitools.controller;

import jakarta.annotation.Generated;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.UUID;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.CreateReminderRequest;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.openapitools.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-02T20:47:24.972943100+02:00[Europe/Warsaw]")
@Controller
@RequestMapping("${openapi.todo.base-path:}")
public class TaskApiController implements TaskApi {

	private final TaskService taskService;

	public TaskApiController(TaskService taskService) {
		this.taskService = taskService;
	}

	@Override
	public ResponseEntity<TaskResponse> createTask(String userId, TaskCreateRequest taskCreateRequest) throws ApiException {
		return new ResponseEntity<>(taskService.createTask(userId, taskCreateRequest), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> deleteTask(String userId, UUID taskId) throws ApiException {
		taskService.deleteTask(userId, taskId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<TaskResponse> getTask(String userId, UUID taskId) throws ApiException {
		return new ResponseEntity<>(taskService.getTaskByUSerIdAndTaskId(userId, taskId), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<TaskResponse>> listTasks(String userId) {
		return new ResponseEntity<>(taskService.getAllTasksByUserId(userId), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<TaskResponse> updateTask(String userId, UUID taskId, TaskUpdateRequest taskUpdateRequest) throws ApiException {
		return new ResponseEntity<>(taskService.updateTask(userId, taskId, taskUpdateRequest), HttpStatus.OK);
	}

	@Override
	@RolesAllowed("GOOGLE")
	public ResponseEntity<TaskResponse> createTaskReminder(UUID taskId, CreateReminderRequest reminderRequest) throws ApiException {
		return new ResponseEntity<>(taskService.createTaskReminder(taskId, reminderRequest), HttpStatus.OK);
	}
}
