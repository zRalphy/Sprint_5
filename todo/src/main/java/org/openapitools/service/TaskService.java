package org.openapitools.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.GlobalUser;
import org.openapitools.model.dto.CreateReminderRequest;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.openapitools.model.entity.Task;
import org.openapitools.model.mapper.TaskMapper;
import org.openapitools.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	private final GoogleCalendarApiService googleCalendarApiService;

	public List<TaskResponse> getAllTasksByUserId() {
		GlobalUser globalUser = (GlobalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Task> taskList = taskRepository.findAllTaskByUserId(globalUser.getId());
		return taskMapper.listTaskToListTaskResponse(taskList);
	}

	public TaskResponse getTaskByUserIdAndTaskId(Long taskId) throws ApiException {
		GlobalUser globalUser = (GlobalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Task task = taskRepository.findTaskByUserIdAndId(globalUser.getId(), taskId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value()));
		return taskMapper.taskToTaskResponse(task);
	}

	public TaskResponse createTask(TaskCreateRequest taskCreateRequest) throws ApiException {
		GlobalUser globalUser = (GlobalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Task> existingTask = taskRepository.findTaskByNameAndUserId(taskCreateRequest.getName(), globalUser.getId());
		if (existingTask.isEmpty()) {
			Task task = new Task();
			task.setName(taskCreateRequest.getName());
			task.setUserId(globalUser.getId());
			task.setDueDate(taskCreateRequest.getDueDate());
			task.setCompleted(false);
			taskRepository.saveAndFlush(task);
			return taskMapper.taskToTaskResponse(task);
		} else {
			throw new ApiException(HttpStatus.CONFLICT.value());
		}
	}

	public TaskResponse updateTask(Long taskId, TaskUpdateRequest taskUpdateRequest) throws ApiException {
		GlobalUser globalUser = (GlobalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Task> existingTask = taskRepository.findTaskByUserIdAndId(globalUser.getId(), taskId);
		if (existingTask.isPresent()) {
			existingTask.get().setName(taskUpdateRequest.getName() != null ? taskUpdateRequest.getName() : existingTask.get().getName());
			existingTask.get().setDueDate(taskUpdateRequest.getDueDate() != null ? taskUpdateRequest.getDueDate() : existingTask.get().getDueDate());
			existingTask.get().setCompleted(taskUpdateRequest.getIsCompleted() != null ? taskUpdateRequest.getIsCompleted() : existingTask.get().isCompleted());
			taskRepository.saveAndFlush(existingTask.get());
			return taskMapper.taskToTaskResponse(existingTask.get());
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND.value());
		}
	}

	public void deleteTask(Long taskId) throws ApiException {
		GlobalUser globalUser = (GlobalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Task> existingTask = taskRepository.findTaskByUserIdAndId(globalUser.getId(), taskId);
		if (existingTask.isPresent()) {
			taskRepository.delete(existingTask.get());
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND.value());
		}
	}

	public TaskResponse createTaskReminder(Long taskId, CreateReminderRequest reminderRequest) throws ApiException {
		Optional<Task> existingTask = taskRepository.findTaskById(taskId);
		if (existingTask.isPresent()) {
			try {
				googleCalendarApiService.createReminder(existingTask.get().getName(), reminderRequest.getStartDateTime(), reminderRequest.getEndDateTime());
			} catch (GeneralSecurityException e) {
				throw new ApiException(HttpStatus.UNAUTHORIZED.value());
			} catch (IOException e) {
				throw new ApiException(HttpStatus.BAD_REQUEST.value());
			}
			return taskMapper.taskToTaskResponse(existingTask.get());
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND.value());
		}
	}
}
