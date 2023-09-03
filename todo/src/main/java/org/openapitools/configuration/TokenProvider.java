package org.openapitools.configuration;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
	public static final String LOGGING_COUNT = "logins_count";
	public static final String CONTENT_TYPE_HEADER = "content-type";
	public static final String AUTHORIZATION_HEADER = "authorization";
	public static final String APPLICATION_JSON_FORMAT = "application/json";
	public static final String SUBSCRIPTIONS_END_URL = "/subscriptions";
	public static final String BEARER = "Bearer ";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String BODY = "{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"audience\":\"%s\",\"grant_type\":\"client_credentials\"}";

	private @Value("${client-id}")
	String clientId;
	private @Value("${client-secret}")
	String clientSecret;
	private @Value("${audience}")
	String audience;
	private @Value("${token-url}")
	String tokenUrl;
	private @Value("${user-url}")
	String userUrl;

	public int getLoggingCount(String userId) throws UnirestException, JsonProcessingException {
		HttpResponse<String> response = Unirest.post(tokenUrl)
				.header(CONTENT_TYPE_HEADER, APPLICATION_JSON_FORMAT)
				.body(String.format(BODY, clientId, clientSecret, audience))
				.asString();

		String token = (String) new ObjectMapper().readValue(response.getBody(), Map.class).get(ACCESS_TOKEN);

		HttpResponse<JsonNode> responseObject = Unirest.get(userUrl + userId)
				.header(AUTHORIZATION_HEADER, BEARER + token).asJson();

		return responseObject.getBody().getObject().getInt(LOGGING_COUNT);
	}

	public String getAccessToken(String clientId, String clientSecret, String audience) throws UnirestException, JsonProcessingException {
		HttpResponse<String> response = Unirest.post(tokenUrl)
				.header(CONTENT_TYPE_HEADER, APPLICATION_JSON_FORMAT)
				.body(String.format(BODY, clientId, clientSecret, audience))
				.asString();
		return (String) new ObjectMapper().readValue(response.getBody(), Map.class).get(ACCESS_TOKEN);
	}
}
