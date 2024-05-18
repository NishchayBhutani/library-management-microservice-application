package com.epam.libraryservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {
	private int id;
	@NotBlank(message = "invalid username")
	private String username;
	@Email(message = "invalid email")
	private String email;
	@NotBlank(message = "invalid message")
	private String name;
}
