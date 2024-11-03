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



    public void markAsRead(String isbn, Integer read) {
        bookRepo.setBookAsRead(isbn, read);
    }

    public Book findBook(String isbn) {
        
        return bookRepo.findByIsbn(isbn);
    }

    public void addBook(String isbn) {
        Book book = findBook(isbn);
        if (book == null){
            final String uri = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + apiKey;
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);
            JSONObject jsonObject = new JSONObject(result);
            String totalItems = jsonObject.get("totalItems").toString();
    
            if(totalItems != "0"){
                JSONArray itemsArr = jsonObject.getJSONArray("items");
                JSONObject itemsObj = itemsArr.getJSONObject(0);
                JSONObject volumeInfo = itemsObj.getJSONObject("volumeInfo");
                JSONArray authorArr = volumeInfo.getJSONArray("authors");
                String author = authorArr.get(0).toString();
                String title = volumeInfo.getString("title");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnail = imageLinks.getString("thumbnail");
    
                Book newBook = new Book();
                newBook.setAuthorName(author);
                newBook.setTitle(title);
                newBook.setIsbn(isbn);
                newBook.setImageLink(thumbnail);
                newBook.setGenre(null);
                newBook.setRead(0);
    
                bookRepo.save(newBook);
            }
        }
        
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }
}
