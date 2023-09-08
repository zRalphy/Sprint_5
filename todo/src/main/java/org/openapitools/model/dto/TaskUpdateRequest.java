package org.openapitools.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * TaskUpdateRequest
 */

public class TaskUpdateRequest {

    private Long id;

    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    private Boolean isCompleted;

    public TaskUpdateRequest id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     */

    @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskUpdateRequest name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     */

    @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("name")
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskUpdateRequest dueDate(LocalDate dueDate) {
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
    public Optional<LocalDate> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskUpdateRequest isCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        return this;
    }

    /**
     * Get isCompleted
     *
     * @return isCompleted
     */

    @Schema(name = "isCompleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("isCompleted")
    public Optional<Boolean> getIsCompleted() {
        return Optional.ofNullable(isCompleted);
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskUpdateRequest taskUpdateRequest = (TaskUpdateRequest) o;
        return Objects.equals(this.id, taskUpdateRequest.id) &&
                Objects.equals(this.name, taskUpdateRequest.name) &&
                Objects.equals(this.dueDate, taskUpdateRequest.dueDate) &&
                Objects.equals(this.isCompleted, taskUpdateRequest.isCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dueDate, isCompleted);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TaskUpdateRequest {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
        sb.append("    isCompleted: ").append(toIndentedString(isCompleted)).append("\n");
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

