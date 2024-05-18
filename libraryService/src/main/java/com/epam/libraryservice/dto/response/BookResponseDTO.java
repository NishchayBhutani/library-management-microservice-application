package com.epam.libraryservice.dto.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class BookResponseDTO {
	@JsonInclude(value = Include.NON_DEFAULT)
	private int id;
	private String name;
	private String publisher;
	private String author;
	private String timeStamp;
	private String developerMessage;
	private HttpStatus httpStatus;
	private String port;
}
