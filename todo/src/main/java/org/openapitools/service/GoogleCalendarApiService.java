package org.openapitools.service;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.configuration.TokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import static org.openapitools.configuration.TokenProvider.ACCESS_TOKEN;
import static org.openapitools.configuration.TokenProvider.AUTHORIZATION_HEADER;
import static org.openapitools.configuration.TokenProvider.BEARER;

@Service
@RequiredArgsConstructor
public class GoogleCalendarApiService {
	private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	private final TokenProvider tokenProvider;

	private @Value("${client-id}")
	String clientId;
	private @Value("${client-secret}")
	String clientSecret;
	private @Value("${audience}")
	String audience;
	private @Value("${user-url}")
	String userUrl;

	public void createReminder(String task, String startDate, String endDate) throws IOException, GeneralSecurityException, UnirestException {
		String token = getIDPAccessToken(getClientData());
		GoogleCredential googleCredential = new GoogleCredential().setAccessToken(token);
		Calendar calendar = new Calendar.Builder(
				GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(),
				googleCredential)
				.setApplicationName("ToDoAppOAuth")
				.build();
		EventDateTime eventStartDateTime = new EventDateTime();
		eventStartDateTime.setDateTime(DateTime.parseRfc3339(startDate));

		EventDateTime eventEndDateTime = new EventDateTime();
		eventEndDateTime.setDateTime(DateTime.parseRfc3339(endDate));

		Event event = new Event();
		event.setSummary(task);
		event.setStart(eventStartDateTime);
		event.setEnd(eventEndDateTime);

		calendar.events().insert("primary", event).execute();
	}

	private String getClientData() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
		OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(),
				authenticationToken.getName());
		return client.getPrincipalName();
	}

	private String getIDPAccessToken(String userId) throws UnirestException, JsonProcessingException {
		String token = tokenProvider.getAccessToken(clientId, clientSecret, audience);
		HttpResponse<JsonNode> responseObject = Unirest.get(userUrl + userId)
				.header(AUTHORIZATION_HEADER, BEARER + token).asJson();
		return responseObject.getBody().getObject().getJSONArray("identities").getJSONObject(0).getString(ACCESS_TOKEN);
	}
}