package org.openapitools.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * TaskCreateRequest
 */

public class TaskCreateRequest {

	private String name;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate dueDate;

	/**
	 * Default constructor
	 *
	 * @deprecated Use {@link TaskCreateRequest#TaskCreateRequest(String)}
	 */
	@Deprecated
	public TaskCreateRequest() {
		super();
	}

	/**
	 * Constructor with only required parameters
	 */
	public TaskCreateRequest(String name) {
		this.name = name;
	}

	public TaskCreateRequest name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get name
	 *
	 * @return name
	 */
	@NotNull
	@Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TaskCreateRequest dueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
		return this;
	}

	/**
	 * Get dueDate
	 *
	 * @return dueDate
	 */
	@Valid
	@Schema(name = "dueDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	@JsonProperty("dueDate")
	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		TaskCreateRequest taskCreateRequest = (TaskCreateRequest) o;
		return Objects.equals(this.name, taskCreateRequest.name) &&
				Objects.equals(this.dueDate, taskCreateRequest.dueDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, dueDate);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class TaskCreateRequest {\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

