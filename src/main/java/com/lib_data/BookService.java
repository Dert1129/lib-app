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

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    public BookService(BookRepository bookRepo){
        this.bookRepo = bookRepo;
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
        
        return bookRepo.findByIsbn(isbn);
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
        System.out.println("making restTemplate");
        
        try {
            System.out.println("inside of try");
            String result = restTemplate.getForObject(uri, String.class);
            JSONObject jsonObject = new JSONObject(result);
    
            // Check if totalItems is 0 before parsing further
            int totalItems = jsonObject.optInt("totalItems", 0);
            System.out.println(totalItems);
    
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
