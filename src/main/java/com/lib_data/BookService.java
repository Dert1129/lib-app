package com.lib_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


@Service
public class BookService {

    private BookRepository bookRepo;
    private RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    public BookService(BookRepository bookRepo, RestTemplate restTemplate){
        this.bookRepo = bookRepo;
        this.restTemplate = restTemplate;
    }

    public String editBook(String isbn, String category, String title, String authorName, String publisher, String genre, Integer copies){
        bookRepo.updateBookByIsbn(title, genre, authorName, category, publisher, copies, isbn);
        return "Updated Book";
        
    }

    public void deleteBook(String isbn) {
        Book book = bookRepo.findByIsbn(isbn);
        if (book != null){
            bookRepo.deleteByIsbn(isbn);
        }
    }

    public void markAsRead(String isbn, Integer read) {
        bookRepo.setBookAsRead(isbn, read);
    }

    public Book findBook(String isbn) {
        if(isbn != null){
            return bookRepo.findByIsbn(isbn);
        }else{
            return null;
        }
        
    }

    public String addManual(String isbn, String category, String title, String publisher, String author, String genre, Integer copies){
        final String uri = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + apiKey;
        try {
            String result = restTemplate.getForObject(uri, String.class);
            JSONObject jsonObject = new JSONObject(result);
            int totalItems = jsonObject.optInt("totalItems", 0);
    
            if (totalItems == 0) {
                return "Book could not be found";
            }else{
                JSONArray itemsArr = jsonObject.getJSONArray("items");
                JSONObject itemsObj = itemsArr.getJSONObject(0);
                JSONObject volumeInfo = itemsObj.getJSONObject("volumeInfo");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnail = imageLinks.getString("thumbnail");
                String description = volumeInfo.optString("description", "");
                Book newBook = new Book();
                newBook.setAuthorName(author);
                newBook.setTitle(title);
                newBook.setIsbn(isbn);
                newBook.setImageLink(thumbnail);
                newBook.setGenre(genre);
                newBook.setRead(0);
                newBook.setCopies(copies);
                newBook.setDescription(description);
                newBook.setCategory(category);
                newBook.setPublisher(publisher);
    
                bookRepo.save(newBook);
                return "Book added successfully";
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return "An error occurred while adding the book"; // Handle exception
        }
    }


    public void addCopy(String isbn){
        Book book = bookRepo.findByIsbn(isbn);
        Integer copies = bookRepo.getCopiesByIsbn(isbn);
        Integer newCopies = copies + 1;
        if(book != null){
            bookRepo.addCopies(isbn, newCopies);
        }
    }

    public String addBook(String isbn) {
        final String uri = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.getForObject(uri, String.class);
            JSONObject jsonObject = new JSONObject(result);
    
            // Check if totalItems is 0 before parsing further
            int totalItems = jsonObject.optInt("totalItems", 0);
    
            if (totalItems == 0) {
                return "Book could not be found";
            } else {
                // Only proceed if totalItems is greater than 0
                JSONArray itemsArr = jsonObject.getJSONArray("items");
                JSONObject itemsObj = itemsArr.getJSONObject(0);
                JSONObject volumeInfo = itemsObj.getJSONObject("volumeInfo");
                JSONArray authorArr = volumeInfo.getJSONArray("authors");
                String author = authorArr.get(0).toString();
                String title = volumeInfo.getString("title");
                String description = volumeInfo.optString("description", "");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnail = imageLinks.getString("thumbnail");
                JSONArray categoriesArr = volumeInfo.getJSONArray("categories");
                String category = categoriesArr.get(0).toString();
                String publisher = volumeInfo.optString("publisher", "Unknown");
                
                Book newBook = new Book();
                newBook.setAuthorName(author);
                newBook.setTitle(title);
                newBook.setIsbn(isbn);
                newBook.setImageLink(thumbnail);
                newBook.setGenre(null);
                newBook.setRead(0);
                newBook.setCopies(1);
                newBook.setDescription(description);
                newBook.setCategory(category);
                newBook.setPublisher(publisher);
    
                bookRepo.save(newBook);
                return "Book added successfully";
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return "An error occurred while adding the book"; // Handle exception
        }
    }
    
    public List<Book> findAll() {
        return bookRepo.findAll();
    }
}
