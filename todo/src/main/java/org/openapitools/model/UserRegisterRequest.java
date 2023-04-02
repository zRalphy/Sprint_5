package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * UserRegisterRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-02T20:47:24.972943100+02:00[Europe/Warsaw]")
public class UserRegisterRequest {

  private String userName;

  private String password;

  private String fullName;

  /**
   * Default constructor
   * @deprecated Use {@link UserRegisterRequest#UserRegisterRequest(String, String, String)}
   */
  @Deprecated
  public UserRegisterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UserRegisterRequest(String userName, String password, String fullName) {
    this.userName = userName;
    this.password = password;
    this.fullName = fullName;
  }

  public UserRegisterRequest userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * Get userName
   * @return userName
  */
  @NotNull 
  @Schema(name = "userName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("userName")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public UserRegisterRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
  */
  @NotNull 
  @Schema(name = "password", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRegisterRequest fullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   * @return fullName
  */
  @NotNull 
  @Schema(name = "fullName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fullName")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRegisterRequest userRegisterRequest = (UserRegisterRequest) o;
    return Objects.equals(this.userName, userRegisterRequest.userName) &&
        Objects.equals(this.password, userRegisterRequest.password) &&
        Objects.equals(this.fullName, userRegisterRequest.fullName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, password, fullName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRegisterRequest {\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
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

