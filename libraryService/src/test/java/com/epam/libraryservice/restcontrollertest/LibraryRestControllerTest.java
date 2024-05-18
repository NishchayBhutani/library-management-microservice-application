package com.epam.libraryservice.restcontrollertest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.libraryservice.dto.LibraryDTO;
import com.epam.libraryservice.dto.request.BookRequestDTO;
import com.epam.libraryservice.dto.request.UserRequestDTO;
import com.epam.libraryservice.dto.response.BookResponseDTO;
import com.epam.libraryservice.dto.response.UserResponseDTO;
import com.epam.libraryservice.repo.BookClient;
import com.epam.libraryservice.repo.UserClient;
import com.epam.libraryservice.restcontroller.LibraryRestController;
import com.epam.libraryservice.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LibraryRestController.class)
class LibraryRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibraryService libraryService;

	@MockBean
	BookClient bookClient;

	@MockBean
	UserClient userClient;

	@Test
	void testGetBookById() throws Exception {
		BookResponseDTO book = BookResponseDTO.builder().id(1).name("Book1").author("Author1").publisher("Publisher1")
				.build();

		when(bookClient.getBookById(1)).thenReturn(ResponseEntity.ok(book));
		mockMvc.perform(get("/library/books/1", 1)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(book.getName())))
				.andExpect(jsonPath("$.publisher", is(book.getPublisher())))
				.andExpect(jsonPath("$.author", is(book.getAuthor()))).andDo(print());
		verify(bookClient).getBookById(1);
	}

	@Test
	void testAddBook() throws Exception {
		BookRequestDTO bookDTO = BookRequestDTO.builder().name("Book 1").publisher("Publisher 1").author("Author 1")
				.build();
		BookResponseDTO responseDTO = BookResponseDTO.builder().name("Book 1").publisher("Publisher 1")
				.author("Author 1").build();
		when(bookClient.addBook(bookDTO)).thenReturn(ResponseEntity.ok(responseDTO));
		mockMvc.perform(post("/library/books").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(bookDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(bookDTO.getName())))
				.andExpect(jsonPath("$.publisher", is(bookDTO.getPublisher())))
				.andExpect(jsonPath("$.author", is(bookDTO.getAuthor())));
		verify(bookClient).addBook(bookDTO);
	}

	@Test
	void testDeleteBookById() throws Exception {
		int bookId = 1;
		doNothing().when(libraryService).deleteBookFromLibrary(bookId);
		mockMvc.perform(delete("/library/books/{bookId}", bookId)).andExpect(status().isNoContent());
		verify(libraryService).deleteBookFromLibrary(bookId);
	}

	@Test
	void testGetAllBooks() throws Exception {
		BookRequestDTO book1 = BookRequestDTO.builder().id(1).name("Book1").author("Author1").publisher("Publisher1")
				.build();
		List<BookRequestDTO> bookDTOList = Arrays.asList(book1);
		BookResponseDTO responseDTO = BookResponseDTO.builder().id(1).name("Book1").author("Author1")
				.publisher("Publisher1").build();
		List<BookResponseDTO> responseDTOList = Arrays.asList(responseDTO);
		Mockito.when(bookClient.getAllBooks()).thenReturn(new ResponseEntity<>(responseDTOList, HttpStatus.OK));
		mockMvc.perform(get("/library/books")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(bookDTOList.size()))
				.andExpect(jsonPath("$.[0].name").value(bookDTOList.get(0).getName()));
		verify(bookClient).getAllBooks();
	}

	@Test
	void testGetUserByUsername() throws Exception {
		String username = "user1";
		BookResponseDTO bookResponse = BookResponseDTO.builder().id(1).name("Book1").author("Author1")
				.publisher("Publisher1").build();
		List<BookResponseDTO> bookDTOList = Arrays.asList(bookResponse);
		Mockito.when(userClient.getUserByUsername(username)).thenReturn(new ResponseEntity<>(
				UserResponseDTO.builder().id(1).username("user1").email("user1@example.com").name("User One").build(),
				HttpStatus.OK));
		Mockito.when(libraryService.getAllBooksByUsername(username)).thenReturn(bookDTOList);
		mockMvc.perform(get("/library/users/{username}", username)).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].username").value(username))
				.andExpect(jsonPath("$.[1].[0].name").value(bookDTOList.get(0).getName()));
		verify(userClient).getUserByUsername(username);
		verify(libraryService).getAllBooksByUsername(username);
	}

	@Test
	void testIssueBooks() throws Exception {
		int bookId = 123;
		String username = "john.doe";
		LibraryDTO libraryDTO = LibraryDTO.builder().username(username).bookId(bookId).build();
		when(libraryService.issueBookToUser(username, bookId)).thenReturn(libraryDTO);
		mockMvc.perform(post("/library/users/{username}/books/{bookId}", username, bookId))
				.andExpect(status().isCreated());
		verify(libraryService, times(1)).issueBookToUser(username, bookId);
	}

	@Test
	void testAddUser() throws Exception {
		UserRequestDTO userDTO = new UserRequestDTO(1, "user1", "user1@example.com", "User One");
		UserResponseDTO responseDTO = UserResponseDTO.builder().id(1).username("user1").email("user1@example.com")
				.name("User One").build();
		Mockito.when(userClient.addUser(userDTO)).thenReturn(new ResponseEntity<>(responseDTO, HttpStatus.CREATED));
		mockMvc.perform(post("/library/users").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		verify(userClient).addUser(userDTO);
	}

	@Test
	void testUpdateUser() throws Exception {
		UserRequestDTO userDTO = new UserRequestDTO(1, "user1", "user1@example.com", "user");
		UserResponseDTO responseDTO = UserResponseDTO.builder().id(1).username("user1").email("user1@example.com")
				.name("user").build();
		Mockito.when(userClient.updateUser("user1", userDTO))
				.thenReturn(new ResponseEntity<>(responseDTO, HttpStatus.OK));
		mockMvc.perform(put("/library/users/{username}", "user1").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDTO))).andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(userDTO.getUsername())))
				.andExpect(jsonPath("$.email", is(userDTO.getEmail())))
				.andExpect(jsonPath("$.name", is(userDTO.getName())));
		verify(userClient).updateUser("user1", userDTO);
	}

	@Test
	void testDeleteUserByUsername() throws Exception {
		String username = "testuser";
		mockMvc.perform(delete("/library/users/" + username).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		verify(userClient).deleteUser(username);
	}

	@Test
	void testGetAllUsers() throws Exception {
		UserRequestDTO userDTO1 = new UserRequestDTO(1, "testuser1", "testuser1@example.com", "Test User 1");
		UserResponseDTO responseDTO = UserResponseDTO.builder().id(1).username("testuser1")
				.email("testuser1@example.com").name("Test User 1").build();
		List<UserResponseDTO> userDTOList = List.of(responseDTO);
		when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok(userDTOList));
		mockMvc.perform(get("/library/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].username", is(userDTO1.getUsername())))
				.andExpect(jsonPath("$.[0].email", is(userDTO1.getEmail())))
				.andExpect(jsonPath("$.[0].name", is(userDTO1.getName())));
		verify(userClient).getAllUsers();
	}

	@Test
	void testWithDrawBookFromUser() throws Exception {
		String username = "testuser";
		int bookId = 123;
		doNothing().when(libraryService).withdrawBookFromUser(username, bookId);
		mockMvc.perform(delete("/library/users/{username}/books/{bookId}", username, bookId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		verify(libraryService).withdrawBookFromUser(username, bookId);
	}

	@Test
	void TeestModifyBook() throws Exception {
		int bookId = 1;
		BookRequestDTO bookDTO = new BookRequestDTO(1, "Book Name", "Publisher", "Author");
		BookResponseDTO responseDTO = BookResponseDTO.builder().id(1).name("Book Name").publisher("Publisher")
				.author("Author").build();
		Mockito.when(bookClient.updateBook(bookId, bookDTO)).thenReturn(ResponseEntity.ok(responseDTO));
		mockMvc.perform(put("/library/books/{id}", bookId).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(bookDTO))).andExpect(status().isOk());
		verify(bookClient).updateBook(bookId, bookDTO);
	}
}
