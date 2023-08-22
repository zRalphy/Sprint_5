package org.openapitools.controller;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.api.ApiException;
import org.openapitools.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("${openapi.todo.base-path:}")
public class UserApiController implements UserApi {
	public static final String OIDC_USER = "OIDC_USER";

	private final NativeWebRequest request;
	private final SubscriptionService subscriptionService;

	private @Value("${client-id}")
	String clientId;
	private @Value("${client-secret}")
	String clientSecret;
	private @Value("${audience}")
	String audience;

	@Override
	@Secured({OIDC_USER})
	public ResponseEntity<String> getUserInfo() throws ApiException {
		Principal token = request.getUserPrincipal();
		String userName;
		if (token instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken defaultToken = (OAuth2AuthenticationToken) request.getUserPrincipal();
			if (defaultToken == null) {
				throw new ApiException(HttpStatus.FORBIDDEN.value());
			}
			userName = defaultToken.getPrincipal().getAttributes().get("name").toString();
			return new ResponseEntity<>(userName, HttpStatus.OK);

		} else {
			throw new ApiException(HttpStatus.FORBIDDEN.value());
		}
	}

	@Override
	public ResponseEntity<String> userRegister(String userId) throws ApiException {
		try {
			subscriptionService.createSubscription(userId, getAccessToken());
		} catch (UnirestException e) {
			throw new ApiException(HttpStatus.CONFLICT.value());
		} catch (JsonProcessingException e) {
			throw new ApiException(HttpStatus.BAD_REQUEST.value());
		}
		return UserApi.super.userRegister(userId);
	}

	private String getAccessToken() throws ApiException {
		HttpResponse<String> response;
		try {
			response = Unirest.post("https://dev-euttml4xgjmuyxo0.eu.auth0.com/oauth/token")
					.header("content-type", "application/json")
					.body(String.format(
							"{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"audience\":\"%s\",\"grant_type\":\"client_credentials\"}",
							clientId, clientSecret, audience))
					.asString();
		} catch (UnirestException e) {
			throw new ApiException(HttpStatus.CONFLICT.value());
		}
		try {
			return (String) new ObjectMapper().readValue(response.getBody(), Map.class).get("access_token");
		} catch (JsonProcessingException e) {
			throw new ApiException(HttpStatus.BAD_REQUEST.value());
		}
	}
}
