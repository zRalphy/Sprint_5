package org.openapitools.configuration;

import lombok.Data;

@Data
public class UserInfo {
    int logins_count;

    public String getIdpAccessToken() {
        return "idptoken";
    }
}