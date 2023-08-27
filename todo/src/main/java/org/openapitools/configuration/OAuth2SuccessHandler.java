package org.openapitools.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.service.SubscriptionService;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	public static final String LOGGING_COUNT = "logins_count";
	public static final int NEW_USER = 1;

	private final SubscriptionService subscriptionService;
	private final TokenProvider tokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
		Pair<String, HttpResponse<JsonNode>> responsePair = null;

		try {
			responsePair = tokenProvider.getToken(authentication);
		} catch (UnirestException | JsonProcessingException e) {
			e.printStackTrace();
		}

		try {
			create7DaysSubscriptionForNewUser(user, Objects.requireNonNull(responsePair).getFirst(), Objects.requireNonNull(responsePair).getSecond());
		} catch (UnirestException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private void create7DaysSubscriptionForNewUser(DefaultOidcUser user, String token, HttpResponse<JsonNode> responseObject)
			throws UnirestException, JsonProcessingException {
		int loggingCount = responseObject.getBody().getObject().getInt(LOGGING_COUNT);
		if (loggingCount == NEW_USER) {
			subscriptionService.createSubscription(user.getSubject(), token);
		}
	}
}