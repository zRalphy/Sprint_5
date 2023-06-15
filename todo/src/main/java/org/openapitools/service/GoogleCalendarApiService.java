package org.openapitools.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class GoogleCalendarApiService {
	OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

	public GoogleCalendarApiService(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
		this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
	}

	public void createReminder(String task, String startDate, String endDate) throws IOException, GeneralSecurityException {
		String token = getTokenFromSession();
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

	private String getTokenFromSession() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
		OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(),
				authenticationToken.getName());
		return client.getAccessToken().getTokenValue();
	}
}
