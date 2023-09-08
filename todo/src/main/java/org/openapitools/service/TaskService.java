package org.openapitools.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ApiException;
import org.openapitools.model.dto.CreateReminderRequest;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.openapitools.model.entity.Task;
import org.openapitools.model.mapper.TaskMapper;
import org.openapitools.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final GoogleCalendarApiService googleCalendarApiService;
    private final SubscriptionService subscriptionService;

    public List<TaskResponse> getAllTasksByUserId(OidcUser oidcUser) {
        String userId = oidcUser.getAttribute("sub");
        List<Task> taskList = taskRepository.findAllTaskByUserId(userId);
        return taskMapper.listTaskToListTaskResponse(taskList);
    }

    public TaskResponse getTaskByUserIdAndTaskId(Long taskId, OidcUser oidcUser) throws ApiException {
        String userId = oidcUser.getAttribute("sub");
        Optional<Task> task = taskRepository.findTaskByUserIdAndId(userId, taskId);
        if (task.isPresent()) {
            return taskMapper.taskToTaskResponse(task.get());
        }
        throw new ApiException(HttpStatus.NOT_FOUND.value());
    }

    public TaskResponse createTask(TaskCreateRequest taskCreateRequest, OidcUser oidcUser) throws ApiException {
        String userId = oidcUser.getAttribute("sub");
        boolean activeSubscription = isActiveSubscription(userId);

        if (activeSubscription) {
            Optional<Task> existingTask = taskRepository.findTaskByNameAndUserId(taskCreateRequest.getName(), userId);
            if (existingTask.isEmpty()) {
                Task task = new Task();
                task.setName(taskCreateRequest.getName());
                task.setUserId(userId);
                task.setDueDate(taskCreateRequest.getDueDate());
                task.setCompleted(false);
                taskRepository.saveAndFlush(task);
                return taskMapper.taskToTaskResponse(task);
            } else {
                throw new ApiException(HttpStatus.CONFLICT.value());
            }
        } else {
            throw new ApiException(HttpStatus.FORBIDDEN.value());
        }
    }

    public TaskResponse updateTask(Long taskId, TaskUpdateRequest taskUpdateRequest, OidcUser oidcUser) throws ApiException {
        String userId = oidcUser.getAttribute("sub");
        boolean activeSubscription = isActiveSubscription(userId);

        if (activeSubscription) {
            return updateTask(taskId, taskUpdateRequest, userId);
        } else {
            throw new ApiException(HttpStatus.FORBIDDEN.value());
        }
    }

    public void deleteTask(Long taskId, OidcUser oidcUser) throws ApiException {
        String userId = oidcUser.getAttribute("sub");
        Optional<Task> existingTask = taskRepository.findTaskByUserIdAndId(userId, taskId);
        if (existingTask.isPresent()) {
            taskRepository.delete(existingTask.get());
        } else {
            throw new ApiException(HttpStatus.NOT_FOUND.value());
        }
    }

    public TaskResponse createTaskReminder(Long taskId, CreateReminderRequest reminderRequest) throws ApiException {
        Task existingTask = taskRepository.getTaskById(taskId);
        googleCalendarApiService.createReminder(existingTask.getName(), reminderRequest.getStartDateTime(), reminderRequest.getEndDateTime());
        return taskMapper.taskToTaskResponse(existingTask);
    }

    private boolean isActiveSubscription(String userId) {
        boolean activeSubscription;
        try {
            activeSubscription = subscriptionService.checkSubscription(userId);
        } catch (UnirestException | JsonProcessingException e) {
            activeSubscription = false;
        }
        return activeSubscription;
    }

    private TaskResponse updateTask(Long taskId, TaskUpdateRequest taskUpdateRequest, String userId) throws ApiException {
        Task existingTask = taskRepository.findTaskByUserIdAndId(userId, taskId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value()));
        taskUpdateRequest.getName().ifPresent(existingTask::setName);
        taskUpdateRequest.getDueDate().ifPresent(existingTask::setDueDate);
        taskUpdateRequest.getIsCompleted().ifPresent(existingTask::setCompleted);
        taskRepository.saveAndFlush(existingTask);
        return taskMapper.taskToTaskResponse(existingTask);
    }
}
