package org.openapitools.configuration;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;

@Component
public class TokenProvider {
	private @Value("${client-id}")
	String clientId;
	private @Value("${client-secret}")
	String clientSecret;
	private @Value("${audience}")
	String audience;

	public Pair<String, HttpResponse<JsonNode>> getToken(Authentication authentication) throws UnirestException, JsonProcessingException {
		DefaultOidcUser user = (DefaultOidcUser) authentication.getPrincipal();
		HttpResponse<String> response = Unirest.post("https://dev-euttml4xgjmuyxo0.eu.auth0.com/oauth/token")
				.header("content-type", "application/json")
				.body(String.format(
						"{\"client_id\":\"%s\",\"client_secret\":\"%s\",\"audience\":\"%s\",\"grant_type\":\"client_credentials\"}",
						clientId, clientSecret, audience))
				.asString();

		String token = (String) new ObjectMapper().readValue(response.getBody(), Map.class).get("access_token");

		HttpResponse<JsonNode> responseObject = Unirest.get(
						"https://dev-euttml4xgjmuyxo0.eu.auth0.com/api/v2/users/" + user.getSubject())
				.header("authorization", "Bearer " + token).asJson();

		return Pair.of(token, responseObject);
	}
}
