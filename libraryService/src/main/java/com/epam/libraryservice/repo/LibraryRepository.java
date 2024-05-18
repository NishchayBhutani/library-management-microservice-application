package com.epam.libraryservice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.libraryservice.dao.Library;

public interface LibraryRepository extends JpaRepository<Library, Integer>{
	void deleteByUsernameAndBookId(String username,int bookId);	
	boolean existsByUsernameAndBookId(String username, int bookId);
	boolean existsByUsername(String username);
	void deleteByBookId(int id);
	void deleteByUsername(String username);
	List<Library> findByUsername(String username);
	int countByUsername(String username);
}