package com.epam.libraryservice.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.epam.libraryservice.dao.Library;
import com.epam.libraryservice.dto.LibraryDTO;
import com.epam.libraryservice.dto.response.BookResponseDTO;
import com.epam.libraryservice.dto.response.UserResponseDTO;
import com.epam.libraryservice.repo.BookClient;
import com.epam.libraryservice.repo.LibraryRepository;
import com.epam.libraryservice.repo.UserClient;
import com.epam.libraryservice.service.LibraryService;

@SpringBootTest
class LibraryServiceTest {

	@Autowired
	private LibraryService libraryService;

	@MockBean
	private LibraryRepository libraryRepository;

	@MockBean
	private UserClient userClient;

	@MockBean
	private BookClient bookClient;

	@MockBean
	private ModelMapper modelMapper;

	@Test
	void TestWithdrawBookFromUser() {
		doNothing().when(libraryRepository).deleteByUsernameAndBookId(anyString(), anyInt());
		libraryService.withdrawBookFromUser("username", 2);
		verify(libraryRepository).deleteByUsernameAndBookId(anyString(), anyInt());
	}

	@Test
	void TestDeleteBookFromLibrary() {
		doNothing().when(libraryRepository).deleteByBookId(anyInt());
		libraryService.deleteBookFromLibrary(16);
		verify(libraryRepository).deleteByBookId(anyInt());
	}

	@Test
	void TestDeleteUserFromLibrary() {
		doNothing().when(libraryRepository).deleteByUsername(anyString());
		libraryService.deleteUserFromLibrary("username");
		verify(libraryRepository).deleteByUsername(anyString());
	}

	@Test
	void TestIssueBookToUserSuccess() {
		String username = "john_doe";
		int bookId = 123;
		Library library = new Library(1, username, bookId);
		LibraryDTO expectedLibraryDTO = new LibraryDTO(1, username, bookId);
		when(bookClient.getBookById(bookId)).thenReturn(ResponseEntity.ok(new BookResponseDTO()));
		when(userClient.getUserByUsername(username)).thenReturn(ResponseEntity.ok(new UserResponseDTO()));
		when(libraryRepository.countByUsername(username)).thenReturn(2);
		when(modelMapper.map(any(Library.class), eq(LibraryDTO.class))).thenReturn(expectedLibraryDTO);
		when(libraryRepository.save(any(Library.class))).thenReturn(library);
		LibraryDTO actualLibraryDTO = libraryService.issueBookToUser(username, bookId);
		assertEquals(expectedLibraryDTO, actualLibraryDTO);
		verify(bookClient).getBookById(bookId);
		verify(userClient).getUserByUsername(username);
		verify(libraryRepository).countByUsername(username);
		verify(libraryRepository).save(any(Library.class));
	}

	@Test
	void testIssueBookToUserMaximumBooksReached() {
		String username = "john_doe";
		int bookId = 123;
		when(bookClient.getBookById(bookId))
				.thenReturn(new ResponseEntity<BookResponseDTO>(new BookResponseDTO(), HttpStatus.OK));
		when(userClient.getUserByUsername(username))
				.thenReturn(new ResponseEntity<UserResponseDTO>(new UserResponseDTO(), HttpStatus.OK));
		when(libraryRepository.countByUsername(username)).thenReturn(3);
		LibraryDTO response = libraryService.issueBookToUser(username, bookId);
		assertEquals("can't issue more than 3 books", response.getDeveloperMessage());
		verify(bookClient).getBookById(bookId);
		verify(userClient).getUserByUsername(username);
		verify(libraryRepository).countByUsername(username);
	}

	@Test
	void issueNewBookToUserInvalidInputs() {
		String username = "test_user";
		int bookId = 123;
		when(userClient.getUserByUsername(username))
				.thenReturn(ResponseEntity.ok(UserResponseDTO.builder().developerMessage("User not found").build()));
		when(bookClient.getBookById(bookId))
				.thenReturn(ResponseEntity.ok(BookResponseDTO.builder().developerMessage("Book not found").build()));
		LibraryDTO result = libraryService.issueBookToUser(username, bookId);
		assertNotNull(result);
		assertEquals(HttpStatus.NO_CONTENT, result.getHttpStatus());
		assertEquals("invalid inputs", result.getDeveloperMessage());
	}

	@Test
	void testGetAllBooksByUsername() {
		List<Library> libraryList = new ArrayList<>();
		List<BookResponseDTO> bookDTOList = new ArrayList<>();
		when(libraryRepository.findByUsername("testuser")).thenReturn(libraryList);
		when(bookClient.getAllBooks()).thenReturn(ResponseEntity.ok(bookDTOList));
		List<BookResponseDTO> resultList = libraryService.getAllBooksByUsername("testuser");
		assertEquals(bookDTOList, resultList);
		verify(libraryRepository).findByUsername("testuser");
		verify(bookClient).getAllBooks();
	}
}
