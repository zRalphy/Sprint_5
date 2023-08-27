package org.openapitools.service;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.model.dto.SubscriptionDTO;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

	public boolean checkSubscription(String userId, String token) throws UnirestException, JsonProcessingException {
		HttpResponse<String> response = Unirest.get("http://localhost:9090/api/user/" + userId + "/subscriptions")
				.header("content-type", "application/json")
				.header("authorization", "Bearer " + token)
				.asString();

		SubscriptionDTO subscription = new ObjectMapper().registerModule(new JavaTimeModule())
				.readValue(response.getBody(), SubscriptionDTO.class);
		return checkDates(subscription);
	}

	public void createSubscription(String userId, String token) throws UnirestException, JsonProcessingException {
		SubscriptionDTO subscriptionDTO = new SubscriptionDTO(OffsetDateTime.now(), OffsetDateTime.now().plusDays(7));
		String body = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(subscriptionDTO);
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
}
