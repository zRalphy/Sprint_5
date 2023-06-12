package org.openapitools.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.aspectj.apache.bcel.classfile.annotation.NameValuePair;
import org.openapitools.api.ApiException;
import org.openapitools.model.dto.CreateReminderRequest;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.openapitools.model.entity.Task;
import org.openapitools.model.mapper.TaskMapper;
import org.openapitools.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
	private final TaskRepository taskRepository;
	private final TaskMapper taskMapper;
	private String eventUrl;
	private String apiKey;

	public TaskService(TaskRepository taskRepository, TaskMapper taskMapper,
			@Value("${googleEventsUrl}") String eventUrl, @Value("${googleApiKey}") String apiKey ) {
		this.taskRepository = taskRepository;
		this.taskMapper = taskMapper;
		this.eventUrl = eventUrl;
		this.apiKey = apiKey;
	}

	public List<TaskResponse> getAllTasksByUserId(String userId) {
		List<Task> taskList = taskRepository.findAllTaskByUserId(Long.parseLong(userId));
		return taskMapper.listTaskToListTaskResponse(taskList);
	}

	public TaskResponse getTaskByUSerIdAndTaskId(String userId, UUID taskId) throws ApiException {
		Task task = taskRepository.findTaskByUserIdAndId(Long.parseLong(userId), taskId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value()));
		return taskMapper.taskToTaskResponse(task);
	}

	public TaskResponse createTask(String userId, TaskCreateRequest taskCreateRequest) throws ApiException {
		Optional<Task> existingTask = taskRepository.findTaskByNameAndUserId(taskCreateRequest.getName(), Long.parseLong(userId));
		if (!existingTask.isPresent()) {
			Task task = new Task();
			task.setName(taskCreateRequest.getName());
			task.setUserId(Long.parseLong(userId));
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

	public TaskResponse createTaskReminder(UUID taskId, CreateReminderRequest reminderRequest) throws ApiException, GeneralSecurityException, IOException {
		Optional<Task> existingTask = taskRepository.findTaskById(taskId);
		if (existingTask.isPresent()) {
			Calendar calendar = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), new JacksonFactory(), new GoogleCredential()).build();
			calendar.get

			return new TaskResponse(UUID.randomUUID(), "test");
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND.value());
		}
	}
}
