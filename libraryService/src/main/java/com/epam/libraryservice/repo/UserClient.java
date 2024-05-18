package com.epam.libraryservice.repo;

import java.util.List;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.libraryservice.dto.request.UserRequestDTO;
import com.epam.libraryservice.dto.response.UserResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@FeignClient(name = "user-service", fallback = UserClientImpl.class)
@LoadBalancerClient(name = "user-service")
public interface UserClient {

	@GetMapping("users")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers();

	@GetMapping("users/{username}")
	public ResponseEntity<UserResponseDTO> getUserByUsername(
			@PathVariable @Valid @NotBlank(message = "invalid username") String username);

	@PostMapping("users")
	public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Valid UserRequestDTO userRequestDTO);

	@DeleteMapping("users/{username}")
	public ResponseEntity<Void> deleteUser(
			@PathVariable @Valid @NotBlank(message = "invalid username") String username);

	@PutMapping("users/{username}")
	public ResponseEntity<UserResponseDTO> updateUser(
			@PathVariable @Valid @NotBlank(message = "invalid username") String username,
			@RequestBody @Valid UserRequestDTO userRequestDTO);

}
