package org.openapitools.service;

import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.configuration.TokenProvider;
import org.openapitools.model.dto.SubscriptionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.openapitools.configuration.TokenProvider.APPLICATION_JSON_FORMAT;
import static org.openapitools.configuration.TokenProvider.AUTHORIZATION_HEADER;
import static org.openapitools.configuration.TokenProvider.BEARER;
import static org.openapitools.configuration.TokenProvider.CONTENT_TYPE_HEADER;
import static org.openapitools.configuration.TokenProvider.SUBSCRIPTIONS_END_URL;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
	private final TokenProvider tokenProvider;

	private @Value("${client-id}")
	String clientId;
	private @Value("${client-secret}")
	String clientSecret;
	private @Value("${subscription-audience}")
	String subscriptionsAudience;
	private @Value("${subscriptions-api-url}")
	String baseSubscriptionsApiUrl;

	public boolean checkSubscription(String userId) throws UnirestException, JsonProcessingException {
		String token = tokenProvider.getAccessToken(clientId, clientSecret, subscriptionsAudience);
		HttpResponse<String> response = Unirest.get(baseSubscriptionsApiUrl + userId + SUBSCRIPTIONS_END_URL)
				.header(CONTENT_TYPE_HEADER, APPLICATION_JSON_FORMAT)
				.header(AUTHORIZATION_HEADER, BEARER + token)
				.asString();

		SubscriptionDTO subscription = new ObjectMapper().registerModule(new JavaTimeModule())
				.readValue(response.getBody(), SubscriptionDTO.class);
		return checkDates(subscription);
	}

	public void createSubscription(String userId) throws UnirestException, JsonProcessingException {
		SubscriptionDTO subscriptionDTO = new SubscriptionDTO(OffsetDateTime.now(), OffsetDateTime.now().plusDays(7));
		String body = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(subscriptionDTO);
		String token = tokenProvider.getAccessToken(clientId, clientSecret, subscriptionsAudience);

		Unirest.post(baseSubscriptionsApiUrl + userId + SUBSCRIPTIONS_END_URL)
				.header(CONTENT_TYPE_HEADER, APPLICATION_JSON_FORMAT)
				.header(AUTHORIZATION_HEADER, BEARER + token)
				.body(body)
				.asJson();
	}

	private boolean checkDates(SubscriptionDTO subscription) {
		return subscription.getStartDateTime().isBefore(OffsetDateTime.now()) &&
				subscription.getEndDateTime().isAfter(OffsetDateTime.now());
	}
}
