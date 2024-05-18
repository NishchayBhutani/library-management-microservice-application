package com.epam.bookservice.servicetest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.epam.bookservice.dao.Book;
import com.epam.bookservice.dto.BookDTO;
import com.epam.bookservice.repo.BookRepository;
import com.epam.bookservice.service.BookService;

@ExtendWith(MockitoExtension.class)
class BooksServiceTest {

	@InjectMocks
	private BookService bookService;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private ModelMapper modelMapper;

	@Test
	void testAddBook() {
		BookDTO bookDTO = new BookDTO();
		bookDTO.setName("Java for Beginners");
		bookDTO.setAuthor("John Smith");
		bookDTO.setPublisher("ABC Publications");

		Book book = new Book();
		book.setId(1);
		book.setName("Java for Beginners");
		book.setAuthor("John Smith");
		book.setPublisher("ABC Publications");

		when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
		when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);
		when(bookRepository.save(book)).thenReturn(book);

		BookDTO addedBook = bookService.addBook(bookDTO);

		assertEquals(bookDTO.getName(), addedBook.getName());
		assertEquals(bookDTO.getAuthor(), addedBook.getAuthor());
		assertEquals(bookDTO.getPublisher(), addedBook.getPublisher());
	}

	@Test
	void testRemoveBook() {
		int id = 1;
		bookService.deleteBook(id);
		Mockito.verify(bookRepository, Mockito.times(1)).deleteById(id);
	}

	@Test
	void testViewById() {
		int id = 1;
		Book book = new Book(id, "Book 1", "Publisher 1", "Author 1");
		Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
		BookDTO bookDTO = new BookDTO(id, "Book 1", "Publisher 1", "Author 1");
		Mockito.when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);
		BookDTO result = bookService.getBookById(id);
		Assertions.assertEquals(bookDTO, result);
	}

	@Test
	void testModifyBook() {
		int id = 1;
		BookDTO bookDTO = new BookDTO(id, "Modified Book 1", "Modified Publisher 1", "Modified Author 1");
		Book book = new Book(id, "Book 1", "Publisher 1", "Author 1");
		Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));
		Mockito.when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);
		BookDTO result = bookService.updateBook(id, bookDTO);
		Assertions.assertEquals(bookDTO, result);
	}

	@Test
	void testModifyBookWithNonExistingId() {
		int id = 1;
		BookDTO bookDTO = new BookDTO(id, "Modified Book 1", "Modified Publisher 1", "Modified Author 1");
		BookDTO result = bookService.updateBook(id, bookDTO);
		assertEquals("no book found with given id", result.getDeveloperMessage());
	}

	@Test
	void testViewAllBooks() {
		Book book1 = new Book(1, "Book 1", "Publisher 1", "Author 1");
		Book book2 = new Book(2, "Book 2", "Publisher 2", "Author 2");
		List<Book> books = Arrays.asList(book1, book2);
		Mockito.when(bookRepository.findAll()).thenReturn(books);
		BookDTO bookDto1 = new BookDTO(1, "Book 1", "Publisher 1", "Author 1");
		BookDTO bookDto2 = new BookDTO(2, "Book 2", "Publisher 2", "Author 2");
		List<BookDTO> expectedBookDtos = Arrays.asList(bookDto1, bookDto2);
		Mockito.when(modelMapper.map(book1, BookDTO.class)).thenReturn(bookDto1);
		Mockito.when(modelMapper.map(book2, BookDTO.class)).thenReturn(bookDto2);
		List<BookDTO> actualBookDtos = bookService.getAllBooks();
		assertThat(actualBookDtos).isEqualTo(expectedBookDtos);
	}

	@Test
	void testViewByIdWhenBookNotFound() {
		int id = 1;
		when(bookRepository.findById(id)).thenReturn(Optional.empty());
		BookDTO result = bookService.getBookById(id);
		assertEquals("no book found with given id", result.getDeveloperMessage());
	}
}
