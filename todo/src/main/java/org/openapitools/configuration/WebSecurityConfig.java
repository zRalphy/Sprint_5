package org.openapitools.configuration;

import org.openapitools.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;

	public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder,
			AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		UserFilter userFilter = new UserFilter();
		userFilter.setAuthenticationManager(createAuthenticationManager());
		userFilter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

		return http
				.cors().disable()
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.and()
				.authorizeHttpRequests()
				.requestMatchers("/user/**").permitAll()
				.requestMatchers("/oauth2/**").permitAll()
				.requestMatchers("/user/info").authenticated()
				.anyRequest().authenticated()
				.and()
				.formLogin().and()
				.oauth2Login()
				.userInfoEndpoint().userService(this.userService)
				.and()
				.successHandler(authenticationSuccessHandler)
				.and()
				.addFilterBefore(userFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	public AuthenticationManager createAuthenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}
}
