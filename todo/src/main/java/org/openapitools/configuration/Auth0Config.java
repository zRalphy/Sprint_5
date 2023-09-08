package org.openapitools.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.RowSet;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth0")
public class Auth0Config {

    Auth0Api subscriptionApi;
    Auth0Api managementApi;
    String tokenUrl;
    String clientId;
    String clientSecret;



    @Data
    public static class Auth0Api {
        String url;
        String audience;
    }
}
