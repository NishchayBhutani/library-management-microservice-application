package com.epam.libraryservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequestDTO {
	private int id;
	@NotBlank(message = "invalid book name")
	private String name;
	@NotBlank(message = "invalid publisher name")
	private String publisher;
	@NotBlank(message = "invalid author name")
	private String author;
}
