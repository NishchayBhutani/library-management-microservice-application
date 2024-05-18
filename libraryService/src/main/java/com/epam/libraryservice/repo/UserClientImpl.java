package com.epam.libraryservice.repo;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.epam.libraryservice.dto.request.UserRequestDTO;
import com.epam.libraryservice.dto.response.UserResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Service
public class UserClientImpl implements UserClient {

	private static final String SERVICE_DOWN = "service down";
	
	@Override
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		UserResponseDTO user = UserResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		List<UserResponseDTO> responseBody = Arrays.asList(user);
		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserResponseDTO> getUserByUsername(
			@Valid @NotBlank(message = "invalid username") String username) {
		UserResponseDTO user = UserResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserResponseDTO> addUser(@Valid UserRequestDTO userRequestDTO) {
		UserResponseDTO user = UserResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> deleteUser(@Valid @NotBlank(message = "invalid username") String username) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<UserResponseDTO> updateUser(@Valid @NotBlank(message = "invalid username") String username,
			@Valid UserRequestDTO userRequestDTO) {
		UserResponseDTO user = UserResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

}
