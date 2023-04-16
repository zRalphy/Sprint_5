package org.openapitools.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.openapitools.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
	List<Task> findAllTaskByUserId(long userId);
	Optional<Task> findTaskByUserIdAndId(long userId, UUID taskId);
	Optional<Task> findTaskByNameAndUserId(String name, long userId);
}
