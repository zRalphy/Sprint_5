package org.openapitools.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.configuration.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	public static final int NEW_USER = 1;

	private final SubscriptionService subscriptionService;
	private final TokenProvider tokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
		try {
			int loggingCount = tokenProvider.getLoggingCount(authentication);
			create7DaysSubscriptionForNewUser(user, loggingCount);
		} catch (UnirestException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private void create7DaysSubscriptionForNewUser(DefaultOidcUser user, int loggingCount)
			throws UnirestException, JsonProcessingException {
		if (loggingCount == NEW_USER) {
			subscriptionService.createSubscription(user.getSubject());
		}
	}
}