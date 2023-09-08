package org.openapitools.service;

import lombok.AllArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.configuration.Auth0Service;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OAuth2UserService extends OidcUserService {
	public static final int NEW_USER = 1;

	private final SubscriptionService subscriptionService;
	private final Auth0Service tokenProvider;

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser user = super.loadUser(userRequest);
		String userId = user.getSubject();
		try {
			int loggingCount = tokenProvider.getLoggingCount(userId);
			create7DaysSubscriptionForNewUser(userId, loggingCount);
		} catch (UnirestException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return super.loadUser(userRequest);
	}

	private void create7DaysSubscriptionForNewUser(String userId, int loggingCount)
			throws UnirestException, JsonProcessingException {
		if (loggingCount == NEW_USER) {
			subscriptionService.createSubscription(userId);
		}
	}
}
