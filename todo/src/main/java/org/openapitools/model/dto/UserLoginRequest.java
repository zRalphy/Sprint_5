package org.openapitools.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserLoginRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-02T20:47:24.972943100+02:00[Europe/Warsaw]")
public class UserLoginRequest {

	private String userName;

	private String password;

	/**
	 * Default constructor
	 *
	 * @deprecated Use {@link UserLoginRequest#UserLoginRequest(String, String)}
	 */
	@Deprecated
	public UserLoginRequest() {
		super();
	}

	/**
	 * Constructor with only required parameters
	 */
	public UserLoginRequest(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public UserLoginRequest userName(String userName) {
		this.userName = userName;
		return this;
	}

	/**
	 * Get userName
	 *
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

	public UserLoginRequest password(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Get password
	 *
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserLoginRequest userLoginRequest = (UserLoginRequest) o;
		return Objects.equals(this.userName, userLoginRequest.userName) &&
				Objects.equals(this.password, userLoginRequest.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userName, password);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserLoginRequest {\n");
		sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
		sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

