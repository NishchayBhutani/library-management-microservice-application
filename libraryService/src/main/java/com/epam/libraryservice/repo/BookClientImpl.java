package com.epam.libraryservice.repo;

import java.util.Arrays;
import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.epam.libraryservice.dto.request.BookRequestDTO;
import com.epam.libraryservice.dto.response.BookResponseDTO;

import jakarta.validation.Valid;

@Service
public class BookClientImpl implements BookClient {

	private static final String SERVICE_DOWN = "service down";

	@Override
	public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
		BookResponseDTO book = BookResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		return new ResponseEntity<>(Arrays.asList(book), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BookResponseDTO> getBookById(@Valid @Range(min = 1, message = "invalid id") int bookId) {
		BookResponseDTO book = BookResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<BookResponseDTO> addBook(@Valid BookRequestDTO bookRequestDTO) {
		BookResponseDTO book = BookResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> deleteBook(@Valid @Range(min = 1, message = "invalid id") int bookId) {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<BookResponseDTO> updateBook(@Valid @Range(min = 1, message = "invalid id") int bookId,
			@Valid BookRequestDTO bookRequestDTO) {
		BookResponseDTO book = BookResponseDTO.builder().developerMessage(SERVICE_DOWN).build();
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

}
