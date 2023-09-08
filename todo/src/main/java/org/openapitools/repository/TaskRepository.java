package org.openapitools.repository;

import java.util.List;
import java.util.Optional;

import org.openapitools.api.ApiException;
import org.openapitools.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllTaskByUserId(String userId);

    Optional<Task> findTaskByUserIdAndId(String userId, long taskId);

    Optional<Task> findTaskByNameAndUserId(String name, String userId);

    Optional<Task> findTaskById(long taskId);

    default Task getTaskById(Long taskId) throws ApiException {
        return findTaskById(taskId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND.value()));
    }
}
