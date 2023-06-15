package org.openapitools.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

import org.openapitools.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
		Map<String, Object> userAttributes = defaultOAuth2User.getAttributes();
		userService.registerOauthUser(userAttributes.get("email").toString(), userAttributes.get("name").toString());
		response.sendRedirect("/");
	}
}
