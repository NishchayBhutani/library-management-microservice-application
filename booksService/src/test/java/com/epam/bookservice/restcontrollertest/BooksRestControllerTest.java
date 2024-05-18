package com.epam.bookservice.restcontrollertest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.bookservice.dto.BookDTO;
import com.epam.bookservice.restcontroller.BookRestController;
import com.epam.bookservice.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookRestController.class)
class BooksRestControllerTest {

    @MockBean
    private BookService bookService;
    
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateBook() throws Exception {
        BookDTO bookDTO = BookDTO.builder()
                .name("Book Name")
                .publisher("Publisher Name")
                .author("Author Name")
                .build();

        when(bookService.addBook(any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(bookDTO.getName())))
                .andExpect(jsonPath("$.publisher", is(bookDTO.getPublisher())))
                .andExpect(jsonPath("$.author", is(bookDTO.getAuthor())));
    }

    @Test
    void testDeleteBook() throws Exception {
        int bookId = 1;

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    void testDisplayAllBook() throws Exception {
        BookDTO bookDTO1 = BookDTO.builder()
                .id(1)
                .name("Book Name 1")
                .publisher("Publisher Name 1")
                .author("Author Name 1")
                .build();

        BookDTO bookDTO2 = BookDTO.builder()
                .id(2)
                .name("Book Name 2")
                .publisher("Publisher Name 2")
                .author("Author Name 2")
                .build();

        List<BookDTO> bookDTOList = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.getAllBooks()).thenReturn(bookDTOList);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookDTO1.getId())))
                .andExpect(jsonPath("$.[0].name", is(bookDTO1.getName())))
                .andExpect(jsonPath("$.[0].publisher", is(bookDTO1.getPublisher())))
                .andExpect(jsonPath("$.[0].author", is(bookDTO1.getAuthor())))
                .andExpect(jsonPath("$.[1].id", is(bookDTO2.getId())))
                .andExpect(jsonPath("$.[1].name", is(bookDTO2.getName())))
                .andExpect(jsonPath("$.[1].publisher", is(bookDTO2.getPublisher())))
                .andExpect(jsonPath("$.[1].author", is(bookDTO2.getAuthor())));
    }

    @Test
    void testDisplayBookById() throws Exception {
        int bookId = 1;
        BookDTO bookDTO = BookDTO.builder()
                .id(bookId)
                .name("Book Name")
                .publisher("Publisher Name")
                .author("Author Name")
                .build();

        when(bookService.getBookById(bookId)).thenReturn(bookDTO);

        mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookDTO.getId())))
                .andExpect(jsonPath("$.name", is(bookDTO.getName())))
                .andExpect(jsonPath("$.publisher", is(bookDTO.getPublisher())))
                .andExpect(jsonPath("$.author", is(bookDTO.getAuthor())));
    }
    
    @Test
    void testModifyBookById() throws Exception {
        int bookId = 1;
        BookDTO bookDTO = BookDTO.builder()
                                  .name("The Lord of the Rings")
                                  .author("J.R.R. Tolkien")
                                  .publisher("HarperCollins")
                                  .build();

        when(bookService.updateBook(eq(bookId), any(BookDTO.class))).thenReturn(bookDTO);

        mockMvc.perform(put("/books/{id}", bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(bookDTO.getName())))
                .andExpect(jsonPath("$.author", is(bookDTO.getAuthor())))
                .andExpect(jsonPath("$.publisher", is(bookDTO.getPublisher())));
    }
}
