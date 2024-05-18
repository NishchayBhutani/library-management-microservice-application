package com.epam.userservice.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
@JsonInclude(value = Include.NON_NULL)
public class UserDTO {
	public UserDTO(int i, String username, String email, String name) {
	}
	private int id;
	@NotBlank(message = "invalid username")
	private String username;
	@Email(message = "invalid email")
	private String email;
	@NotBlank(message = "invalid message")
	private String name;
	private String timeStamp;
	private String developerMessage;
	private HttpStatus httpStatus;
	private String port;
}
