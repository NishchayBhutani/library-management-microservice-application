package com.epam.libraryservice.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class LibraryDTO {
	public LibraryDTO(int i, String username, int bookId) {
	}
	@JsonInclude(value = Include.NON_DEFAULT)
	private int id;
	private String username;
	private int bookId;
	private String timeStamp;
	private String developerMessage;
	private HttpStatus httpStatus;
}
