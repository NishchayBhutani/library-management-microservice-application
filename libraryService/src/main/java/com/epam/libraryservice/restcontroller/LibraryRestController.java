package com.epam.libraryservice.restcontroller;

import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.epam.libraryservice.dto.LibraryDTO;
import com.epam.libraryservice.dto.request.BookRequestDTO;
import com.epam.libraryservice.dto.request.UserRequestDTO;
import com.epam.libraryservice.dto.response.BookResponseDTO;
import com.epam.libraryservice.dto.response.UserResponseDTO;
import com.epam.libraryservice.repo.BookClient;
import com.epam.libraryservice.repo.UserClient;
import com.epam.libraryservice.service.LibraryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("library")
public class LibraryRestController {

	@Autowired
	LibraryService libraryService;

	@Autowired
	UserClient userClient;

	@Autowired
	BookClient bookClient;

	@GetMapping("/books")
	public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
		log.info("get request to get all books");
		return bookClient.getAllBooks();
	}

	@GetMapping("/books/{bookId}")
	public ResponseEntity<BookResponseDTO> getBookById(
			@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId) {
		log.info("get request to get book by id: {}", bookId);
		return bookClient.getBookById(bookId);
	}

	@PostMapping("/books")
	public ResponseEntity<BookResponseDTO> addBook(@RequestBody @Valid BookRequestDTO bookDTO) {
		log.info("post request to add book: {}", bookDTO);
		return bookClient.addBook(bookDTO);
	}

	@PutMapping("/books/{bookId}")
	public ResponseEntity<BookResponseDTO> modifyBook(
			@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId,
			@RequestBody @Valid BookRequestDTO bookDTO) {
		log.info("put request to update book: {}", bookDTO);
		return bookClient.updateBook(bookId, bookDTO);
	}

	@DeleteMapping("/books/{bookId}")
	public ResponseEntity<Void> deleteBookById(
			@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId) {
		log.info("delete request to delete book with id: {}", bookId);
		libraryService.deleteBookFromLibrary(bookId);
		bookClient.deleteBook(bookId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		log.info("get request to get all users");
		return userClient.getAllUsers();
	}

	@GetMapping("/users/{username}")
	public ResponseEntity<List<Object>> getUserByUsername(@PathVariable String username) {
		log.info("get request to get user by username: {}", username);
		ResponseEntity<UserResponseDTO> responseEntity = userClient.getUserByUsername(username);
		return new ResponseEntity<>(List.of(responseEntity.getBody(), libraryService.getAllBooksByUsername(username)),
				HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<UserResponseDTO> addUser(@RequestBody @Valid UserRequestDTO userDTO) {
		log.info("post request to add user: {}", userDTO);
		return userClient.addUser(userDTO);
	}

	@PutMapping("/users/{username}")
	public ResponseEntity<UserResponseDTO> updateUser(
			@PathVariable @Valid @NotBlank(message = "username invalid") String username,
			@RequestBody @Valid UserRequestDTO userDTO) {
		log.info("post request to update user: {}", userDTO);
		return userClient.updateUser(username, userDTO);
	}

	@DeleteMapping("/users/{username}")
	public ResponseEntity<Void> deleteUserByUsername(
			@PathVariable @Valid @NotBlank(message = "username invalid") String username) {
		log.info("delete request to delete user with username: {}", username);
		libraryService.deleteUserFromLibrary(username);
		userClient.deleteUser(username);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/users/{username}/books/{bookId}")
	public ResponseEntity<LibraryDTO> issueBookToUser(
			@PathVariable @Valid @NotBlank(message = "username invalid") String username,
			@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId) {
		log.info("post request to issue book: {} to user: {}", bookId, username);
		LibraryDTO libraryDTO = libraryService.issueBookToUser(username, bookId);
		return new ResponseEntity<>(libraryDTO,
				libraryDTO.getDeveloperMessage() == null ? HttpStatus.CREATED : HttpStatus.OK);
	}

	@DeleteMapping("/users/{username}/books/{bookId}")
	public ResponseEntity<Void> withdrawBookFromUser(
			@PathVariable @Valid @NotBlank(message = "username invalid") String username,
			@PathVariable @Valid @Range(min = 1, message = "invalid id") int bookId) {
		log.info("delete request to take book: {} from user: {}", bookId, username);
		libraryService.withdrawBookFromUser(username, bookId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
