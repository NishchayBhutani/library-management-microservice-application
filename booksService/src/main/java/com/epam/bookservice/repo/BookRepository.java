package com.epam.bookservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.bookservice.dao.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

}
