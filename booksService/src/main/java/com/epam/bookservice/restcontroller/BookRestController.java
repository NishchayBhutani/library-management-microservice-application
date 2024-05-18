package com.epam.bookservice.restcontroller;

import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.bookservice.dto.BookDTO;
import com.epam.bookservice.service.BookService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("books")
public class BookRestController {

	@Autowired
	BookService bookService;

	@Autowired
	Environment env;

	@GetMapping
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		log.info("get request to get all books");
		List<BookDTO> list = bookService.getAllBooks().stream().map(bookDTO -> {
			bookDTO.setPort(env.getProperty("local.server.port"));
			return bookDTO;
		}).toList();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<BookDTO> getBookById(
			@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId) {
		log.info("get request to get book by id: {}", bookId);
		BookDTO result = bookService.getBookById(bookId);
		result.setPort(env.getProperty("local.server.port"));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<BookDTO> addBook(@RequestBody @Valid BookDTO bookDTO) {
		log.info("post request to add book: {}", bookDTO);
		BookDTO result = bookService.addBook(bookDTO);
		result.setPort(env.getProperty("local.server.port"));
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBook(@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId) {
		log.info("delete request for book: {}", bookId);
		bookService.deleteBook(bookId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/{bookId}")
	public ResponseEntity<BookDTO> updateBook(@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId,
			@RequestBody @Valid BookDTO bookDTO) {
		log.info("put request for book: {}", bookDTO);
		BookDTO result = bookService.updateBook(bookId, bookDTO);
		result.setPort(env.getProperty("local.server.port"));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

}