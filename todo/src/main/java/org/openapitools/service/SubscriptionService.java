package org.openapitools.service;

import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.model.dto.SubscriptionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
	private @Value("${client-id}")
	String clientId;
	private @Value("${client-secret}")
	String clientSecret;
	private @Value("${audience}")
	String audience;

	public boolean checkSubscription(String userId) throws UnirestException, JsonProcessingException {
		String token = getAccessToken();
		HttpResponse<String> response = Unirest.get("http://localhost:9090/api/user/" + userId + "/subscriptions")
				.header("content-type", "application/json")
				.header("authorization", "Bearer " + token)
				.asString();

		SubscriptionDTO subscription = new ObjectMapper().registerModule(new JavaTimeModule())
				.readValue(response.getBody(), SubscriptionDTO.class);
		return checkDates(subscription);
	}

	public void createSubscription(String userId) throws UnirestException, JsonProcessingException {
		SubscriptionDTO subscriptionDTO = new SubscriptionDTO(OffsetDateTime.now(), OffsetDateTime.now().plusDays(7));
		String body = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(subscriptionDTO);
		String token = getAccessToken();

		Unirest.post("http://localhost:9090/api/user/" + userId + "/subscriptions")
				.header("content-type", "application/json")
				.header("authorization", "Bearer " + token)
				.body(body)
				.asJson();
	}

	private boolean checkDates(SubscriptionDTO subscription) {
		return subscription.getStartDateTime().isBefore(OffsetDateTime.now()) &&
				subscription.getEndDateTime().isAfter(OffsetDateTime.now());
	}

	protected String getAccessToken() throws UnirestException, JsonProcessingException {
		HttpResponse<String> response = Unirest.post("https://dev-euttml4xgjmuyxo0.eu.auth0.com/oauth/token")
				.header("content-type", "application/json")
				.body(String.format(
						"{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"audience\":\"%s\",\"grant_type\":\"client_credentials\"}",
						clientId, clientSecret, audience))
				.asString();
		return (String) new ObjectMapper().readValue(response.getBody(), Map.class).get("access_token");
	}
}
