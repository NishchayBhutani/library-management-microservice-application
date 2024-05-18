package com.epam.libraryservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
	String timestamp;
	String status;
	String error;
	String path;
}
