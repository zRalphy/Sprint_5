package org.openapitools.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.UserRegisterRequest;
import org.openapitools.model.entity.GlobalUser;
import org.openapitools.model.entity.User;
import org.openapitools.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends DefaultOAuth2UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User registerUser(UserRegisterRequest userLoginRequest) throws ApiException {
		Optional<User> existingUser = userRepository.findUserByUserName(userLoginRequest.getUserName());
		if (!existingUser.isPresent()) {
			User newUser = new User();
			newUser.setUserName(userLoginRequest.getUserName());
			newUser.setPassword(passwordEncoder.encode(userLoginRequest.getPassword()));
			newUser.setFullName(userLoginRequest.getFullName());
			newUser.setProvider(User.Provider.LOCAL);
			return userRepository.save(newUser);
		} else {
			throw new ApiException(HttpStatus.CONFLICT.value());
		}
	}

	@Override
	public GlobalUser loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findUserByUserName(userName).get();
		GlobalUser globalUser = new GlobalUser(Collections.singleton(
				new SimpleGrantedAuthority(String.valueOf(user.getId()))), Map.of("userName", user.getUserName()), "userName");
		globalUser.setId(user.getId());
		globalUser.setUserName(user.getUserName());
		globalUser.setPassword(user.getPassword());
		return globalUser;
	}

	public User registerOauthUser(String userName, String fullName) {
		Optional<User> existingUser = userRepository.findUserByUserName(userName);
		if (!existingUser.isPresent()) {
			User newUser = new User();
			newUser.setUserName(userName);
			newUser.setFullName(fullName);
			newUser.setProvider(User.Provider.GOOGLE);
			return userRepository.save(newUser);
		} else {
			return existingUser.get();
		}
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		User user = userRepository.findUserByUserName(oAuth2User.getAttributes().get("email").toString()).get();
		GlobalUser globalUser = new GlobalUser(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "sub");
		globalUser.setId(user.getId());
		globalUser.setUserName(user.getUserName());
		globalUser.setPassword(user.getPassword());
		return globalUser;
	}
}

