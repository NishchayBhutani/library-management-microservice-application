package com.epam.libraryservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epam.libraryservice.dao.Library;
import com.epam.libraryservice.dto.LibraryDTO;
import com.epam.libraryservice.dto.response.BookResponseDTO;
import com.epam.libraryservice.repo.BookClient;
import com.epam.libraryservice.repo.LibraryRepository;
import com.epam.libraryservice.repo.UserClient;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LibraryService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	LibraryRepository libraryRepository;

	@Autowired
	UserClient userClient;

	@Autowired
	BookClient bookClient;

	public LibraryDTO issueBookToUser(String username, int bookId) {
		log.info("issuing book to user");
		LibraryDTO result;
		if (Optional.of(bookClient.getBookById(bookId).getBody()).get().getDeveloperMessage() != null
				|| Optional.of(userClient.getUserByUsername(username).getBody()).get().getDeveloperMessage() != null) {
			log.error("invalid inputs");
			result = LibraryDTO.builder().developerMessage("invalid inputs").httpStatus(HttpStatus.NO_CONTENT)
					.timeStamp(new Date().toString()).build();
		} 
		else if (libraryRepository.countByUsername(username) == 3) {
			log.error("can't issue more than 3 books");
			result = LibraryDTO.builder().developerMessage("can't issue more than 3 books")
					.httpStatus(HttpStatus.NO_CONTENT).timeStamp(new Date().toString()).build();
		} else {
			Library library = Library.builder().id(bookId).username(username).build();
			result = modelMapper.map(libraryRepository.save(library), LibraryDTO.class);
		}
		return result;
	}

	@Transactional
	public void withdrawBookFromUser(String username, int bookId) {
		log.info("taking book from user");
		libraryRepository.deleteByUsernameAndBookId(username, bookId);
	}

	@Transactional
	public void deleteBookFromLibrary(int id) {
		log.info("deleting book from library");
		libraryRepository.deleteByBookId(id);
	}

	@Transactional
	public void deleteUserFromLibrary(String username) {
		log.info("deleting user from library");
		libraryRepository.deleteByUsername(username);
	}

	public List<BookResponseDTO> getAllBooksByUsername(String username) {
		log.info("getting all book ids by username");
		List<Integer> bookIdList = libraryRepository.findByUsername(username).stream().map(Library::getBookId).toList();
		Optional<List<BookResponseDTO>> optionalBookList = Optional.of(bookClient.getAllBooks().getBody());
		return optionalBookList.get().stream().filter(book -> bookIdList.contains(book.getId())).toList();
	}
}