package org.openapitools.model.mapper;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.openapitools.model.dto.TaskResponse;
import org.openapitools.model.entity.Task;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TaskMapper {

	public TaskResponse taskToTaskResponse(Task task) {
		return new TaskResponse(task.getId(), task.getName())
				.dueDate(task.getDueDate())
				.isDone(task.isCompleted());
	}

	public List<TaskResponse> listTaskToListTaskResponse(List<Task> tasks) {
		List<TaskResponse> taskResponseList = new ArrayList<>(tasks.size());
		for (Task task : tasks) {
			TaskResponse taskResponse = new TaskResponse(task.getId(), task.getName())
					.dueDate(task.getDueDate())
					.isDone(task.isCompleted());
			taskResponseList.add(taskResponse);
		}
		return taskResponseList;
	}
}
