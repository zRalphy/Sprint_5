package org.openapitools.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.CreateReminderRequest;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.openapitools.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import static org.openapitools.controller.UserApiController.OIDC_USER;

@Controller
@RequiredArgsConstructor
@RequestMapping("${openapi.todo.base-path:}")
public class TaskApiController implements TaskApi {
	private final TaskService taskService;
	private final NativeWebRequest request;

	@Override
	@RolesAllowed(OIDC_USER)
	public ResponseEntity<TaskResponse> getTask(Long taskId, @AuthenticationPrincipal OidcUser oidcUser) throws ApiException {
		return new ResponseEntity<>(taskService.getTaskByUserIdAndTaskId(taskId, oidcUser), HttpStatus.OK);
	}

	@Override
	@RolesAllowed(OIDC_USER)
	public ResponseEntity<List<TaskResponse>> listTasks(@AuthenticationPrincipal OidcUser oidcUser) {
		return new ResponseEntity<>(taskService.getAllTasksByUserId(oidcUser), HttpStatus.OK);
	}

	@Override
	@RolesAllowed(OIDC_USER)
	public ResponseEntity<TaskResponse> createTask(TaskCreateRequest taskCreateRequest, @AuthenticationPrincipal OidcUser oidcUser) throws ApiException {
		return new ResponseEntity<>(taskService.createTask(taskCreateRequest, oidcUser), HttpStatus.OK);
	}

	@Override
	@RolesAllowed(OIDC_USER)
	public ResponseEntity<TaskResponse> updateTask(Long taskId, TaskUpdateRequest taskUpdateRequest, @AuthenticationPrincipal OidcUser oidcUser)
			throws ApiException {
		return new ResponseEntity<>(taskService.updateTask(taskId, taskUpdateRequest, oidcUser), HttpStatus.OK);
	}

	@Override
	@RolesAllowed(OIDC_USER)
	public ResponseEntity<Void> deleteTask(Long taskId, @AuthenticationPrincipal OidcUser oidcUser) throws ApiException {
		taskService.deleteTask(taskId, oidcUser);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	@RolesAllowed(OIDC_USER)
	public ResponseEntity<TaskResponse> createTaskReminder(Long taskId, CreateReminderRequest reminderRequest) throws ApiException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) request.getUserPrincipal();
		DefaultOidcUser oidcUser = (DefaultOidcUser) token.getPrincipal();
		if (oidcUser.getSubject().contains("google")) {
			return new ResponseEntity<>(taskService.createTaskReminder(taskId, reminderRequest), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}
}
