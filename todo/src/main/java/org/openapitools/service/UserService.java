package org.openapitools.service;

import java.util.Collections;
import java.util.Optional;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.UserLoginRequest;
import org.openapitools.model.dto.UserRegisterRequest;
import org.openapitools.model.entity.User;
import org.openapitools.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
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
			newUser.setPassword((passwordEncoder.encode(userLoginRequest.getPassword())));
			newUser.setFullName(userLoginRequest.getFullName());
			newUser.setProvider(User.Provider.LOCAL);
			return userRepository.save(newUser);
		} else {
			throw new ApiException(HttpStatus.CONFLICT.value());
		}
	}

	public User loginUser(final UserLoginRequest userLoginRequest) throws ApiException {
		Optional<User> existingUser = userRepository.findUserByUserName(userLoginRequest.getUserName());
		if (existingUser.isPresent()) {
			String password = existingUser.get().getPassword();
			if (passwordEncoder.matches(userLoginRequest.getPassword(), password)) {
				return existingUser.get();
			} else {
				throw new ApiException(HttpStatus.UNAUTHORIZED.value());
			}
		} else {
			throw new ApiException(HttpStatus.NOT_FOUND.value());
		}
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findUserByUserName(userName).get();
		return org.springframework.security.core.userdetails.User
				.withUsername(userName)
				.password(user.getPassword())
				.authorities(Collections.singleton(new SimpleGrantedAuthority(String.valueOf(user.getId()))))
				.build();
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
}

