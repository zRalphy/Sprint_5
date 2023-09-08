package org.openapitools.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Auth0Service {
    public static final String CONTENT_TYPE_HEADER = "content-type";
    public static final String AUTHORIZATION_HEADER = "authorization";
    public static final String APPLICATION_JSON_FORMAT = "application/json";
    public static final String SUBSCRIPTIONS_END_URL = "/subscriptions";
    public static final String BEARER = "Bearer ";
    public static final String ACCESS_TOKEN = "access_token";

    private final Auth0Config auth0Config;

    public int getLoggingCount(String userId) throws UnirestException, JsonProcessingException {
        UserInfo userInfo = getUserInfo(userId);
        return userInfo.getLogins_count();
    }

    public UserInfo getUserInfo(String userId) throws UnirestException, JsonProcessingException {
        String token = getAccessToken(auth0Config.getManagementApi().getAudience());
        UserInfo userInfo = Unirest.get(String.format(auth0Config.getManagementApi().getUrl(), userId))
                .header(AUTHORIZATION_HEADER, BEARER + token)
                .asObject(UserInfo.class).getBody();
        return userInfo;
    }

    public String getAccessToken(String audience) throws UnirestException, JsonProcessingException {
        TokenRequst tokenRequst = TokenRequst.builder()
                .client_id(auth0Config.clientId)
                .client_secret(auth0Config.getClientSecret())
                .audience(audience)
                .grant_type("client_credentials")
                .build();
        HttpResponse<String> response = Unirest.post(auth0Config.getTokenUrl())
                .header(CONTENT_TYPE_HEADER, APPLICATION_JSON_FORMAT)
                .body(tokenRequst)
                .asString();
        return (String) new ObjectMapper().readValue(response.getBody(), Map.class).get(ACCESS_TOKEN);
    }

    @Builder
    static class TokenRequst {
        String client_id;
        String client_secret;
        String audience;
        String grant_type;
    }

}
