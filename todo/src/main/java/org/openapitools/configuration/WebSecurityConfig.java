package org.openapitools.configuration;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import static org.openapitools.controller.UserApiController.OIDC_USER;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http
				.cors().disable()
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.and()
				.authorizeHttpRequests()
				.requestMatchers("/user/**").permitAll()
				.requestMatchers("/oauth2/**").permitAll()
				.requestMatchers("/user/info").hasRole(OIDC_USER)
				.anyRequest().authenticated()
				.and()
				.oauth2Login(oauth2 -> oauth2
						.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
								.oidcUserService(oidcUserService)))
				.build();
	}
}
