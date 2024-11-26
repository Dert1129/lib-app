package com.lib_data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.context.TestPropertySource;
@TestPropertySource("classpath:application-test.properties")
public class BookServiceTest {
    
    @Mock
    private BookRepository bookRepo;
    @InjectMocks
    private BookService bookService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		bookService = new BookService(bookRepo, restTemplate);
	}

	@AfterEach
	public void tearDown() throws Exception {
		MockitoAnnotations.openMocks(this).close();
        bookService = null;
	}

    @Test
    public void testgetBook_whenTotalItemsIsZero_thenReturnBookNotFound() {
        String isbn = "1234567890";

        // Mock RestTemplate response to simulate API returning totalItems as 0
        String jsonResponse = new JSONObject()
                .put("kind", "books#volumes")
                .put("totalItems", 0)
                .toString();

        // Mock the RestTemplate call to return our simulated response
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        // Execute the method under test
        Book result = bookService.getBook(isbn);

        // Verify the result
        assertEquals(null, result);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Book> expectedBooks = Arrays.asList(mock(Book.class));
        when(bookRepo.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.findAll();

        assertEquals(expectedBooks, actualBooks);
        verify(bookRepo).findAll();

    }

    @Test
    void testDeleteBook_BookExists() {
        Integer id = 1;
        Book book = new Book();
        book.setId(id);
        when(bookRepo.findBookById(id)).thenReturn(book);

        bookService.deleteBook(id);

        verify(bookRepo, times(1)).deleteById(id);
    }

    @Test
    void testgetBook_BookDoesNotExistAndIsFetched() {
        String isbn = "1234567890";
        
        when(bookService.findBookByIsbn(isbn)).thenReturn(null);
    
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalItems", 1); 
        JSONArray itemsArray = new JSONArray();
        JSONObject item = new JSONObject();
        JSONObject volumeInfo = new JSONObject();
        volumeInfo.put("title", "Mock Title");
        volumeInfo.put("description", "Mock Description");
        volumeInfo.put("publisher", "Mock Publisher");
        JSONArray authorsArray = new JSONArray();
        authorsArray.put("Mock Author");
        volumeInfo.put("authors", authorsArray);
        JSONArray categoriesArray = new JSONArray();
        categoriesArray.put("Mock Category");
        volumeInfo.put("categories", categoriesArray);
        JSONObject imageLinks = new JSONObject();
        imageLinks.put("thumbnail", "http://mock-thumbnail-url.com");
        volumeInfo.put("imageLinks", imageLinks);
        item.put("volumeInfo", volumeInfo);
        itemsArray.put(item);
        jsonObject.put("items", itemsArray);
    
        when(restTemplate.getForObject(anyString(), eq(String.class)))
            .thenReturn(jsonObject.toString());

        bookService.getBook(isbn);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepo, times(1)).save(bookCaptor.capture());

        Book savedBook = bookCaptor.getValue();
        System.out.println("Saved Book: " + savedBook);
        assertEquals("Mock Title", savedBook.getTitle());
        assertEquals("Mock Description", savedBook.getDescription());
        assertEquals("Mock Author", savedBook.getAuthorName());
    }

    @Test
    void testgetBook_BookAlreadyExists() {
        String isbn = "1234567890";
        Book existingBook = new Book();
        when(bookService.findBookByIsbn(isbn)).thenReturn(existingBook);

        bookService.getBook(isbn);

        verify(bookRepo, never()).save(any(Book.class));
        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void testgetBook_BookNotFoundInApi() {
        String isbn = "1234567890";
        when(bookService.findBookByIsbn(isbn)).thenReturn(null);
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalItems", 0);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonObject.toString());

        bookService.getBook(isbn);

        verify(bookRepo, never()).save(any(Book.class));
    }

    @Test
    void testgetBook_InvalidApiKey() {
    String isbn = "1234567890";
    when(bookService.findBookByIsbn(isbn)).thenReturn(null);

    when(restTemplate.getForObject(anyString(), eq(String.class)))
        .thenThrow(new RuntimeException("API key not valid. Please pass a valid API key."));

    assertDoesNotThrow(() -> bookService.getBook(isbn));
    verify(bookRepo, never()).save(any(Book.class));
}
}
