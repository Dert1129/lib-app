package com.lib_data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

public class BookControllerTest {
    
    @Mock
    private BookController controller;
    @Mock
    private BookService bookService;
    @Mock 
    private BookRepository bookRepo;
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        controller = new BookController(bookService,bookRepo);
    }

    @AfterEach
    public void tearDown() throws Exception {
        MockitoAnnotations.openMocks(this).close();
    }

    @Test
    public void testGetBooks() {
        List<Book> books = Arrays.asList(mock(Book.class));
        when(bookService.findAll()).thenReturn(books);
        HttpServletRequest request = mock(HttpServletRequest.class);

        List<Book> result = controller.getBooks(request);

        assertEquals(books, result);
    }

    @Test
    public void testAddBook() {
        
    }
}
