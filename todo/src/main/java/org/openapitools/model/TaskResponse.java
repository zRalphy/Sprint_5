package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * TaskResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-02T20:47:24.972943100+02:00[Europe/Warsaw]")
public class TaskResponse {

  private Long id;

  private String name;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate dueDate;

  private Boolean isDone;

  /**
   * Default constructor
   * @deprecated Use {@link TaskResponse#TaskResponse(Long, String)}
   */
  @Deprecated
  public TaskResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TaskResponse(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public TaskResponse id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @NotNull 
  @Schema(name = "id", example = "58206234", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TaskResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
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

  public TaskResponse dueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  /**
   * Get dueDate
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

  public TaskResponse isDone(Boolean isDone) {
    this.isDone = isDone;
    return this;
  }

  /**
   * Get isDone
   * @return isDone
  */
  
  @Schema(name = "isDone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isDone")
  public Boolean getIsDone() {
    return isDone;
  }

  public void setIsDone(Boolean isDone) {
    this.isDone = isDone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaskResponse taskResponse = (TaskResponse) o;
    return Objects.equals(this.id, taskResponse.id) &&
        Objects.equals(this.name, taskResponse.name) &&
        Objects.equals(this.dueDate, taskResponse.dueDate) &&
        Objects.equals(this.isDone, taskResponse.isDone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, dueDate, isDone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaskResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    isDone: ").append(toIndentedString(isDone)).append("\n");
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

