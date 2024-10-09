package com.lib_data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookServiceTest {
    
    @Mock
    private BookRepository bookRepo;
    private BookService bookService;

    @BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		bookService = new BookService(bookRepo);
	}

	@AfterEach
	public void tearDown() throws Exception {
		MockitoAnnotations.openMocks(this).close();
        bookService = null;
	}

    @Test
    public void testFindAll() throws Exception {
        List<Book> expectedBooks = Arrays.asList(mock(Book.class));
        when(bookRepo.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.findAll();

        assertEquals(expectedBooks, actualBooks);
        verify(bookRepo).findAll();

    }
}
