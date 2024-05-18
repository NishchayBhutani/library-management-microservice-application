package com.epam.bookservice.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class BookDTO {
	public BookDTO(int id2, String string, String string2, String string3) {
	}
	private int id;
	@NotBlank(message = "invalid book name")
	private String name;
	@NotBlank(message = "invalid publisher name")
	private String publisher;
	@NotBlank(message = "invalid author name")
	private String author;
	private String timeStamp;
	private String developerMessage;
	private HttpStatus httpStatus;
	private String port;
}
