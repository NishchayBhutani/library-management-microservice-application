package com.epam.bookservice.exception;

public class BookException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BookException(String message) {
		super(message);
	}
}
