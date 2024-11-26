package com.lib_data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookRepository bookRepo;

    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setIsbn("1234567890");
        book.setCategory("Fiction");
    }

    @Test
    void testEditBook_Success() throws Exception {
        when(bookRepo.findBookById(1)).thenReturn(book);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);
        payload.put("title", "Updated Title");
        payload.put("category", "Updated Category");
        payload.put("publisher", "Updated Publisher");
        payload.put("genre", "Updated Genre");
        payload.put("authorName", "Updated Author");
        payload.put("copies", 5);

        mockMvc.perform(post("/api/editBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated book"));

        verify(bookService, times(1)).editBook(anyInt(), anyString(), anyString(), anyString(), anyString(), anyList(), anyInt(), any(Date.class), any(Date.class));
    }

    @Test
    void testEditBook_Failure() throws Exception {
        when(bookRepo.findBookById(1)).thenReturn(null);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);
        payload.put("title", "Updated Title");
        payload.put("category", "Updated Category");
        payload.put("publisher", "Updated Publisher");
        payload.put("genre", "Updated Genre");
        payload.put("authorName", "Updated Author");
        payload.put("copies", 5);

        mockMvc.perform(post("/api/editBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book could not be updated"));

        verify(bookService, times(0)).editBook(anyInt(), anyString(), anyString(), anyString(), anyString(), anyList(), anyInt(), any(Date.class), any(Date.class));
    }

    @Test
    void testGetBook() throws Exception {
        when(bookService.findBookById(1)).thenReturn(book);

        mockMvc.perform(get("/api/getBook")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        verify(bookService, times(1)).findBookById(1);
    }

    @Test
    void testAddManual_BookExists() throws Exception {
        when(bookRepo.findByIsbn("1234567890")).thenReturn(book);

        Map<String, Object> payload = new HashMap<>();
        payload.put("isbn", "1234567890");
        payload.put("title", "Manual Book");
        payload.put("category", "Fiction");
        payload.put("publisher", "Publisher");
        payload.put("genre", "Genre");
        payload.put("authorName", "Author");
        payload.put("copies", 5);

        mockMvc.perform(post("/api/addManual")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book exists in catelog"));

        verify(bookService, times(0)).addManual(anyString(), anyString(), anyString(), anyString(), anyString(), anyList(), anyInt(), anyString(), anyString());
    }

    @Test
    void testDeleteBook_Success() throws Exception {
        when(bookRepo.findBookById(1)).thenReturn(book);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);

        mockMvc.perform(delete("/api/deleteBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted book with isbn: 1"));

        verify(bookRepo, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBook_BookNotFound() throws Exception {
        when(bookRepo.findBookById(1)).thenReturn(null);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);

        mockMvc.perform(delete("/api/deleteBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Could not delete book with isbn: 1"));

        verify(bookRepo, times(0)).deleteById(1);
    }
}
