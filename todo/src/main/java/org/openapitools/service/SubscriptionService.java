package org.openapitools.service;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openapitools.configuration.Auth0Config;
import org.openapitools.configuration.Auth0Service;
import org.openapitools.model.dto.SubscriptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import static org.openapitools.configuration.Auth0Service.APPLICATION_JSON_FORMAT;
import static org.openapitools.configuration.Auth0Service.AUTHORIZATION_HEADER;
import static org.openapitools.configuration.Auth0Service.BEARER;
import static org.openapitools.configuration.Auth0Service.CONTENT_TYPE_HEADER;
import static org.openapitools.configuration.Auth0Service.SUBSCRIPTIONS_END_URL;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final Auth0Service auth0Service;
    private final Auth0Config auth0Config;

    public boolean checkSubscription(String userId) throws UnirestException, JsonProcessingException {
        String token = auth0Service.getAccessToken(auth0Config.getSubscriptionApi().getUrl());
        SubscriptionDTO subscription = Unirest.get(String.format(auth0Config.getSubscriptionApi().getUrl(), userId))
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON_FORMAT)
                .header(AUTHORIZATION_HEADER, BEARER + token)
                .asObject(SubscriptionDTO.class)
                .getBody();
        return subscription.isActive(OffsetDateTime.now());
    }

    public void createSubscription(String userId) throws UnirestException, JsonProcessingException {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(OffsetDateTime.now(), OffsetDateTime.now().plusDays(7));
        String token = auth0Service.getAccessToken(auth0Config.getSubscriptionApi().getUrl());

        URI uri = UriComponentsBuilder.fromUriString(auth0Config.getSubscriptionApi().getUrl())
                .build(Map.of("user_id", userId));

        Unirest.post(uri.toString())
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON_FORMAT)
                .header(AUTHORIZATION_HEADER, BEARER + token)
                .body(subscriptionDTO)
                .asJson();
    }


}
