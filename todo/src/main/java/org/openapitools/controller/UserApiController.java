package org.openapitools.controller;

import jakarta.annotation.Generated;

import java.security.Principal;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.UserRegisterRequest;
import org.openapitools.model.entity.User;
import org.openapitools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-02T20:47:24.972943100+02:00[Europe/Warsaw]")
@Controller
@RequestMapping("${openapi.todo.base-path:}")
public class UserApiController implements UserApi {

	public static final String OAUTH_USER = "OAUTH_USER";

	private final NativeWebRequest request;
	private final UserService userService;

	@Autowired
	public UserApiController(NativeWebRequest request, UserService userService) {
		this.request = request;
		this.userService = userService;
	}

	@Override
	public ResponseEntity<Void> register(UserRegisterRequest userRegisterRequest) throws ApiException {
		User user = userService.registerUser(userRegisterRequest);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("userId", String.valueOf(user.getId()));
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	@Override
	@Secured({OAUTH_USER})
	public ResponseEntity<String> getUserInfo() throws ApiException {
		Principal token = request.getUserPrincipal();
		String userName;
		if (token instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken defaultToken = (OAuth2AuthenticationToken) request.getUserPrincipal();
			userName = defaultToken.getPrincipal().getAttributes().get("name").toString();
			return new ResponseEntity<>(userName, HttpStatus.OK);
		} else {
			throw new ApiException(HttpStatus.FORBIDDEN.value());
		}
	}
}
