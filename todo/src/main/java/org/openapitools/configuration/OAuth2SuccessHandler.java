package org.openapitools.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import org.openapitools.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final UserService userService;

	public OAuth2SuccessHandler(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
		Map<String, Object> userAttributes = defaultOAuth2User.getAttributes();
		userService.registerOauthUser(userAttributes.get("email").toString(), userAttributes.get("name").toString());
		response.sendRedirect("/");
	}
}
