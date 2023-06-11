package org.openapitools.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.openapitools.model.entity.GlobalUser;
import org.openapitools.model.entity.Task;
import org.openapitools.model.mapper.TaskMapper;
import org.openapitools.repository.TaskRepository;
import org.openapitools.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final TaskMapper taskMapper;

	public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserRepository userRepository) {
		this.taskRepository = taskRepository;
		this.taskMapper = taskMapper;
		this.userRepository = userRepository;
	}

	public List<TaskResponse> getAllTasksByUserId(String userId) {
		List<Task> taskList = taskRepository.findAllTaskByUserId(Long.parseLong(userId));
		return taskMapper.listTaskToListTaskResponse(taskList);
	}

	public TaskResponse getTaskByUSerIdAndTaskId(String userId, UUID taskId) throws ApiException {
		Task task = taskRepository.findTaskByUserIdAndId(Long.parseLong(userId), taskId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value()));
		return taskMapper.taskToTaskResponse(task);
	}
/*
	public TaskResponse createTask(TaskCreateRequest taskCreateRequest) throws ApiException {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof DefaultOAuth2User defaultOAuth2User) {
			Optional<User> userFromDb = userRepository.findUserByUserName(defaultOAuth2User.getAttributes().get("email").toString());
			return createTaskByUser(taskCreateRequest, userFromDb.get().getId());
		} else if (principal instanceof org.springframework.security.core.userdetails.User user) {
			Optional<User> userFromDb = userRepository.findUserByUserName(user.getUsername());
			return createTaskByUser(taskCreateRequest, userFromDb.get().getId());
		}
		throw new ApiException(HttpStatus.UNAUTHORIZED.value());
	}

 */

	public TaskResponse createTask(TaskCreateRequest taskCreateRequest) throws ApiException {
		GlobalUser globalUser = (GlobalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return createTaskByUser(taskCreateRequest, globalUser.getId());
	}

	private TaskResponse createTaskByUser(TaskCreateRequest taskCreateRequest, Long userId) throws ApiException {
		Optional<Task> existingTask = taskRepository.findTaskByNameAndUserId(taskCreateRequest.getName(), userId);
		if (existingTask.isEmpty()) {
			Task task = new Task();
			task.setName(taskCreateRequest.getName());
			task.setUserId(userId);
			task.setDueDate(taskCreateRequest.getDueDate());
			task.setIsCompleted(false);
			taskRepository.saveAndFlush(task);
			return taskMapper.taskToTaskResponse(task);
		} else {
			throw new ApiException(HttpStatus.CONFLICT.value());
		}
	}

	public TaskResponse updateTask(String userId, UUID taskId, TaskUpdateRequest taskUpdateRequest) throws ApiException {
		Optional<Task> existingTask = taskRepository.findTaskByUserIdAndId(Long.parseLong(userId), taskId);
		if (existingTask.isPresent()) {
			existingTask.get().setName(taskUpdateRequest.getName());
			existingTask.get().setDueDate(taskUpdateRequest.getDueDate());
			existingTask.get().setIsCompleted(taskUpdateRequest.getIsCompleted());
			taskRepository.saveAndFlush(existingTask.get());
			return taskMapper.taskToTaskResponse(existingTask.get());
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND.value());
		}
	}

	public void deleteTask(String userId, UUID taskId) throws ApiException {
		Optional<Task> existingTask = taskRepository.findTaskByUserIdAndId(Long.parseLong(userId), taskId);
		if (existingTask.isPresent()) {
			taskRepository.delete(existingTask.get());
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND.value());
		}
	}
}
