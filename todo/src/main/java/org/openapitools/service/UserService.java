package org.openapitools.service;

import java.util.Optional;

import org.openapitools.api.ApiException;
import org.openapitools.model.dto.UserLoginRequest;
import org.openapitools.model.dto.UserRegisterRequest;
import org.openapitools.model.entity.User;
import org.openapitools.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
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
}

