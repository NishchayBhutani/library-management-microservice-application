package com.epam.libraryservice.repo;

import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.libraryservice.dto.request.BookRequestDTO;
import com.epam.libraryservice.dto.response.BookResponseDTO;

import jakarta.validation.Valid;

@FeignClient(name = "book-service", fallback = BookClientImpl.class)
@LoadBalancerClient(name = "book-service")
public interface BookClient {
	
	@GetMapping("books")
	public ResponseEntity<List<BookResponseDTO>> getAllBooks();
	
	@GetMapping("books/{bookId}")
	public ResponseEntity<BookResponseDTO> getBookById(@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId);
	
	@PostMapping("books")
	public ResponseEntity<BookResponseDTO> addBook(@RequestBody @Valid BookRequestDTO bookRequestDTO);
	
	@DeleteMapping("books/{bookId}")
	public ResponseEntity<Void> deleteBook(@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId);
	
	@PutMapping("books/{bookId}")
	public ResponseEntity<BookResponseDTO> updateBook(@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId,
			@RequestBody @Valid BookRequestDTO bookRequestDTO);
	
}
