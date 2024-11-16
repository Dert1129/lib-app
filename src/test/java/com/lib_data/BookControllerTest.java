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
        // Setup a sample book object
        book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setIsbn("1234567890");
        book.setCategory("Fiction");
    }

    @Test
    void testEditBook_Success() throws Exception {
        // Mock bookRepo.findBookById to return a book
        when(bookRepo.findBookById(1)).thenReturn(book);

        // Prepare the payload as a Map
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);
        payload.put("title", "Updated Title");
        payload.put("category", "Updated Category");
        payload.put("publisher", "Updated Publisher");
        payload.put("genre", "Updated Genre");
        payload.put("authorName", "Updated Author");
        payload.put("copies", 5);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/editBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated book"));

        // Verify that bookService.editBook was called
        verify(bookService, times(1)).editBook(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void testEditBook_Failure() throws Exception {
        // Mock bookRepo.findBookById to return null (book doesn't exist)
        when(bookRepo.findBookById(1)).thenReturn(null);

        // Prepare the payload as a Map
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);
        payload.put("title", "Updated Title");
        payload.put("category", "Updated Category");
        payload.put("publisher", "Updated Publisher");
        payload.put("genre", "Updated Genre");
        payload.put("authorName", "Updated Author");
        payload.put("copies", 5);

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/editBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book could not be updated"));

        // Verify that bookService.editBook was not called
        verify(bookService, times(0)).editBook(anyInt(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void testGetBook() throws Exception {
        // Mock bookService.findBookById to return a book
        when(bookService.findBookById(1)).thenReturn(book);

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/getBook")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));

        // Verify that bookService.findBookById was called
        verify(bookService, times(1)).findBookById(1);
    }

    @Test
    void testAddManual_BookExists() throws Exception {
        // Mock bookRepo.findByIsbn to return a book (existing book)
        when(bookRepo.findByIsbn("1234567890")).thenReturn(book);

        // Prepare the payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("isbn", "1234567890");
        payload.put("title", "Manual Book");
        payload.put("category", "Fiction");
        payload.put("publisher", "Publisher");
        payload.put("genre", "Genre");
        payload.put("authorName", "Author");
        payload.put("copies", 5);

        // Perform the POST request
        mockMvc.perform(post("/api/addManual")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Book exists in catelog"));

        // Verify that bookService.addManual was not called
        verify(bookService, times(0)).addManual(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void testDeleteBook_Success() throws Exception {
        // Mock bookRepo.findBookById to return a book
        when(bookRepo.findBookById(1)).thenReturn(book);

        // Prepare the payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/deleteBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted book with isbn: 1"));

        // Verify that bookRepo.deleteById was called
        verify(bookRepo, times(1)).deleteById(1);
    }

    @Test
    void testDeleteBook_BookNotFound() throws Exception {
        // Mock bookRepo.findBookById to return null
        when(bookRepo.findBookById(1)).thenReturn(null);

        // Prepare the payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", 1);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/deleteBook")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string("Could not delete book with isbn: 1"));

        // Verify that bookRepo.deleteById was not called
        verify(bookRepo, times(0)).deleteById(1);
    }
}
