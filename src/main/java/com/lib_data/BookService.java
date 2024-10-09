package com.lib_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class BookService {

    private BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo){
        this.bookRepo = bookRepo;
    }

    public void addBook(String isbn) {
        final String uri = "https://openlibrary.org/isbn/" + isbn + ".json";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        JSONObject jsonObj = new JSONObject(result);
        String title = jsonObj.getString("title");
        JSONArray authorArray = jsonObj.getJSONArray("authors");
        JSONObject authorObj = authorArray.getJSONObject(0);
        String authorEndpoint = authorObj.getString("key");
        final String authorAPI = "https://openlibrary.org" + authorEndpoint + ".json";
        String authorResult = restTemplate.getForObject(authorAPI, String.class);
        JSONObject authObject = new JSONObject(authorResult);
        String authorName = authObject.getString("name");

        Book newBook = new Book();
        newBook.setAuthorName(authorName);
        newBook.setTitle(title);
        newBook.setIsbn(isbn);

        bookRepo.save(newBook);
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }
}
