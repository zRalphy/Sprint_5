package org.openapitools.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String name;
	private LocalDate dueDate;
	private long userId;
	boolean isCompleted;

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
}
