package org.openapitools.controller;

import jakarta.annotation.Generated;

import java.util.Optional;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.UserLoginRequest;
import org.openapitools.model.dto.UserRegisterRequest;
import org.openapitools.model.entity.User;
import org.openapitools.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-04-02T20:47:24.972943100+02:00[Europe/Warsaw]")
@Controller
@RequestMapping("${openapi.todo.base-path:}")
public class UserApiController implements UserApi {

	private final NativeWebRequest request;
	private final UserService userService;

	@Autowired
	public UserApiController(NativeWebRequest request, UserService userService) {
		this.request = request;
		this.userService = userService;
	}

	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	@Override
	@SessionScope
	public ResponseEntity<Void> login(UserLoginRequest userLoginRequest) throws ApiException {
		User user = userService.loginUser(userLoginRequest);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword());
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);


		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("userId", String.valueOf(user.getId()));
		headers.add("cookie", String.valueOf(user.getId()));

		return new ResponseEntity<>(headers, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<Void> register(UserRegisterRequest userRegisterRequest) throws ApiException {
		User user = userService.registerUser(userRegisterRequest);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("userId", String.valueOf(user.getId()));
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
}
