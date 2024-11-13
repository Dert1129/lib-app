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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.context.TestPropertySource;
@TestPropertySource("classpath:application-test.properties")
public class BookServiceTest {
    
    @Mock
    private BookRepository bookRepo;
    private BookService bookService;
    @Mock
    private RestTemplate restTemplate;

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
    public void testAddBook_whenTotalItemsIsZero_thenReturnBookNotFound() {
        String isbn = "1234567890";

        // Mock RestTemplate response to simulate API returning totalItems as 0
        String jsonResponse = new JSONObject()
                .put("kind", "books#volumes")
                .put("totalItems", 0)
                .toString();

        // Mock the RestTemplate call to return our simulated response
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        // Execute the method under test
        String result = bookService.addBook(isbn);

        // Verify the result
        assertEquals("Book could not be found", result);
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
        String isbn = "1234567890";
        Book book = new Book();
        book.setIsbn(isbn);
        when(bookRepo.findByIsbn(isbn)).thenReturn(book);

        bookService.deleteBook(isbn);

        verify(bookRepo, times(1)).deleteByIsbn(isbn);
    }

    @Test
    void testDeleteBook_BookDoesNotExist() {
        String isbn = "1234567890";
        when(bookRepo.findByIsbn(isbn)).thenReturn(null);

        bookService.deleteBook(isbn);

        verify(bookRepo, never()).deleteByIsbn(anyString());
    }

   @Test
void testAddBook_BookDoesNotExistAndIsFetched() {
    String isbn = "1234567890";
    when(bookService.findBook(isbn)).thenReturn(null);

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

    when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonObject.toString());
    
    bookService.addBook(isbn);

    verify(bookRepo, times(1)).save(any(Book.class));
}


    @Test
    void testAddBook_BookAlreadyExists() {
        String isbn = "1234567890";
        Book existingBook = new Book();
        when(bookService.findBook(isbn)).thenReturn(existingBook);

        bookService.addBook(isbn);

        verify(bookRepo, never()).save(any(Book.class));
        verify(restTemplate, never()).getForObject(anyString(), eq(String.class));
    }

    @Test
    void testAddBook_BookNotFoundInApi() {
        String isbn = "1234567890";
        when(bookService.findBook(isbn)).thenReturn(null);
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalItems", 0);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonObject.toString());

        bookService.addBook(isbn);

        verify(bookRepo, never()).save(any(Book.class));
    }

    @Test
    void testAddBook_InvalidApiKey() {
    String isbn = "1234567890";
    when(bookService.findBook(isbn)).thenReturn(null);

    when(restTemplate.getForObject(anyString(), eq(String.class)))
        .thenThrow(new RuntimeException("API key not valid. Please pass a valid API key."));

    assertDoesNotThrow(() -> bookService.addBook(isbn));
    verify(bookRepo, never()).save(any(Book.class));
}
}
