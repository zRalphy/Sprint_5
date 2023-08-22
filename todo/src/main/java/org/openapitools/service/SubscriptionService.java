package org.openapitools.service;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.model.dto.SubscriptionDTO;

public class SubscriptionService {

	public boolean checkSubscription(String userId, String token) throws UnirestException, JsonProcessingException {
		HttpResponse<String> response = Unirest.get("https://localhost/9090/api/user/" + userId + "/subscriptions")
				.header("content-type", "application/json")
				.header("authorization", "barer " + token)
				.asString();

		SubscriptionDTO subscription = new ObjectMapper().readValue(response.getBody(), SubscriptionDTO.class);
		return checkDates(subscription);

	}

	public boolean createSubscription(String userId, String token) throws UnirestException, JsonProcessingException {
		HttpResponse<String> response = Unirest.post("https://localhost/9090/api/user/" + userId + "/subscriptions")
				.header("content-type", "application/json")
				.header("authorization", "barer " + token)
				.asString();

		SubscriptionDTO subscription = new ObjectMapper().readValue(response.getBody(), SubscriptionDTO.class);
		return checkDates(subscription);

	}

	private boolean checkDates(SubscriptionDTO subscription) {
		return subscription.getStartDateTime().isBefore(OffsetDateTime.now()) &&
				subscription.getEndDateTime().isAfter(OffsetDateTime.now());
	}
}
