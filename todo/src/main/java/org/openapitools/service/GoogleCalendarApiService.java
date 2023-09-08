package org.openapitools.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.ApiException;
import org.openapitools.configuration.Auth0Service;
import org.openapitools.configuration.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class GoogleCalendarApiService {
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final Auth0Service tokenProvider;

    public void createReminder(String task, String startDate, String endDate) throws ApiException {
        try {
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
        } catch (GeneralSecurityException | IOException | UnirestException e) {
            throw new ApiException(500);
        }
    }

    private String getClientData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName());
        return client.getPrincipalName();
    }

    private String getIDPAccessToken(String userId) throws UnirestException, JsonProcessingException {
        UserInfo userInfo = tokenProvider.getUserInfo(userId);
        return userInfo.getIdpAccessToken();
    }
}