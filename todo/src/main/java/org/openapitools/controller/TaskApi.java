package org.openapitools.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.CreateReminderRequest;
import org.openapitools.model.dto.TaskCreateRequest;
import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.dto.TaskUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-02T20:47:24.972943100+02:00[Europe/Warsaw]")
@Validated
@Tag(name = "tasks", description = "the tasks API")
public interface TaskApi {

	/**
	 * POST /task : Creates new task
	 *
	 * @param userId
	 * 		(required)
	 * @param taskCreateRequest
	 * 		(required)
	 * @return Task created (status code 201)
	 * or Access token is missing or invalid (status code 401)
	 */
	@Operation(
			operationId = "createTask",
			summary = "Creates new task",
			tags = {"tasks"},
			responses = {
					@ApiResponse(responseCode = "201", description = "Task created", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
					}),
					@ApiResponse(responseCode = "401", description = "Access token is missing or invalid")
			}
	)
	@RequestMapping(
			method = RequestMethod.POST,
			value = "/task",
			produces = {"application/json"},
			consumes = {"application/json"}
	)
	default ResponseEntity<TaskResponse> createTask(
			@NotNull
			@Parameter(name = "userId", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "userId") String userId,
			@Parameter(name = "TaskCreateRequest", required = true) @Valid @RequestBody TaskCreateRequest taskCreateRequest)
			throws ApiException {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * DELETE /task/{taskId} : Deletes the task
	 *
	 * @param userId
	 * 		(required)
	 * @param taskId
	 * 		Task identifier (required)
	 * @return Task deleted (status code 204)
	 * or Access token is missing or invalid (status code 401)
	 * or Resource is forbidden (status code 403)
	 * or Resource not found (status code 404)
	 */
	@Operation(
			operationId = "deleteTask",
			summary = "Deletes the task",
			tags = {"tasks"},
			responses = {
					@ApiResponse(responseCode = "204", description = "Task deleted"),
					@ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
					@ApiResponse(responseCode = "403", description = "Resource is forbidden"),
					@ApiResponse(responseCode = "404", description = "Resource not found")
			}
	)
	@RequestMapping(
			method = RequestMethod.DELETE,
			value = "/task/{taskId}"
	)
	default ResponseEntity<Void> deleteTask(
			@NotNull
			@Parameter(name = "userId", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "userId") String userId,
			@Parameter(name = "taskId", description = "Task identifier", required = true, in = ParameterIn.PATH) @PathVariable("taskId") UUID taskId)
			throws ApiException {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * GET /task/{taskId} : Returns task
	 *
	 * @param userId
	 * 		(required)
	 * @param taskId
	 * 		Task identifier (required)
	 * @return Task details (status code 200)
	 * or Access token is missing or invalid (status code 401)
	 * or Resource is forbidden (status code 403)
	 * or Resource not found (status code 404)
	 */
	@Operation(
			operationId = "getTask",
			summary = "Returns task",
			tags = {"tasks"},
			responses = {
					@ApiResponse(responseCode = "200", description = "Task details", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
					}),
					@ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
					@ApiResponse(responseCode = "403", description = "Resource is forbidden"),
					@ApiResponse(responseCode = "404", description = "Resource not found")
			}
	)
	@RequestMapping(
			method = RequestMethod.GET,
			value = "/task/{taskId}",
			produces = {"application/json"}
	)
	default ResponseEntity<TaskResponse> getTask(
			@NotNull
			@Parameter(name = "userId", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "userId") String userId,
			@Parameter(name = "taskId", description = "Task identifier", required = true, in = ParameterIn.PATH) @PathVariable("taskId") UUID taskId)
			throws ApiException {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * GET /task : List all the tasks for a user
	 *
	 * @param userId
	 * 		(required)
	 * @return Tasks list (status code 200)
	 */
	@Operation(
			operationId = "listTasks",
			summary = "List all the tasks for a user",
			tags = {"tasks"},
			responses = {
					@ApiResponse(responseCode = "200", description = "Tasks list", content = {
							@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))
					})
			}
	)
	@RequestMapping(
			method = RequestMethod.GET,
			value = "/task",
			produces = {"application/json"}
	)
	default ResponseEntity<List<TaskResponse>> listTasks(
			@NotNull
			@Parameter(name = "userId", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "userId") String userId) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

	}

	/**
	 * PUT /task/{taskId} : Updates existing task
	 *
	 * @param userId
	 * 		(required)
	 * @param taskId
	 * 		Task identifier (required)
	 * @param taskUpdateRequest
	 * 		(required)
	 * @return Updated task (status code 200)
	 * or Access token is missing or invalid (status code 401)
	 * or Resource is forbidden (status code 403)
	 * or Resource not found (status code 404)
	 */
	@Operation(
			operationId = "updateTask",
			summary = "Updates existing task",
			tags = {"tasks"},
			responses = {
					@ApiResponse(responseCode = "200", description = "Updated task", content = {
							@Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))
					}),
					@ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
					@ApiResponse(responseCode = "403", description = "Resource is forbidden"),
					@ApiResponse(responseCode = "404", description = "Resource not found")
			}
	)
	@RequestMapping(
			method = RequestMethod.PUT,
			value = "/task/{taskId}",
			produces = {"application/json"},
			consumes = {"application/json"}
	)
	default ResponseEntity<TaskResponse> updateTask(
			@NotNull
			@Parameter(name = "userId", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "userId") String userId,
			@Parameter(name = "taskId", description = "Task identifier", required = true, in = ParameterIn.PATH) @PathVariable("taskId") UUID taskId,
			@Parameter(name = "TaskUpdateRequest", required = true) @Valid @RequestBody TaskUpdateRequest taskUpdateRequest)
			throws ApiException {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * POST /task/{taskId}/reminder : Creates reminder in Google Calendar
	 *
	 * @param taskId
	 * 		Task identifier (required)
	 * @return Created task (status code 201)
	 * or Access token is missing or invalid (status code 401)
	 * or Resource is forbidden (status code 403)
	 * or Resource not found (status code 404)
	 */
	@Operation(
			operationId = "createReminder",
			summary = "Creates reminder in Google Calendar",
			tags = {"tasks"},
			responses = {
					@ApiResponse(responseCode = "201", description = "Reminder created", content = {
							@Content(mediaType = "application/json")
					}),
					@ApiResponse(responseCode = "401", description = "Access token is missing or invalid"),
					@ApiResponse(responseCode = "403", description = "Resource is forbidden"),
					@ApiResponse(responseCode = "404", description = "Resource not found")
			}
	)
	@RequestMapping(
			method = RequestMethod.POST,
			value = "/task/{taskId}/reminder",
			produces = {"application/json"},
			consumes = {"application/json"}
	)
	default ResponseEntity<TaskResponse> createTaskReminder(
			@Parameter(name = "taskId", description = "Task identifier", required = true, in = ParameterIn.PATH) @PathVariable("taskId") UUID taskId,
			@Parameter(name = "CreateReminderRequest", required = true) @Valid @RequestBody CreateReminderRequest reminderRequest)
			throws ApiException {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
}
