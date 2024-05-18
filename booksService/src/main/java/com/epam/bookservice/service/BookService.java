package com.epam.bookservice.service;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epam.bookservice.dao.Book;
import com.epam.bookservice.dto.BookDTO;
import com.epam.bookservice.repo.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	ModelMapper modelMapper;

	private static final String NOT_FOUND = "no book found with given id";

	public List<BookDTO> getAllBooks() {
		log.info("getting all books");
		return bookRepository.findAll().stream().map(book -> modelMapper.map(book, BookDTO.class)).toList();
	}

	public BookDTO getBookById(int id) {
		log.info("getting book by id: {}", id);
		return bookRepository.findById(id).map(book -> modelMapper.map(book, BookDTO.class))
				.orElseGet(() -> BookDTO.builder().developerMessage(NOT_FOUND).timeStamp(new Date().toString())
						.httpStatus(HttpStatus.NO_CONTENT).build());
	}

	public BookDTO addBook(BookDTO bookDTO) {
		log.info("adding book: {}", bookDTO);
		try {
			return modelMapper.map(bookRepository.save(modelMapper.map(bookDTO, Book.class)), BookDTO.class);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("same book already exists");
		}
	}

	public void deleteBook(int id) {
		log.info("deleting book by id: {}", id);
		bookRepository.deleteById(id);
	}

	public BookDTO updateBook(int id, BookDTO bookDTO) {
		log.info("updating book: {}", bookDTO);
		return bookRepository.findById(id).map(book -> {
			book.setName(bookDTO.getName());
			book.setAuthor(bookDTO.getAuthor());
			book.setPublisher(bookDTO.getPublisher());
			return modelMapper.map(book, BookDTO.class);
		}).orElseGet(() -> BookDTO.builder().developerMessage(NOT_FOUND).timeStamp(new Date().toString())
				.httpStatus(HttpStatus.NO_CONTENT).build());

	}
}
