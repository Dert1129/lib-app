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

    public void addBook(String isbn) {
        Book book = findBook(isbn);
        if (book == null){
            final String uri = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + apiKey;
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);
            JSONObject jsonObject = new JSONObject(result);
            String totalItems = jsonObject.get("totalItems").toString();
            Integer totalItemsInt = Integer.parseInt(totalItems);
    
            if(totalItemsInt != 0){
                JSONArray itemsArr = jsonObject.getJSONArray("items");
                JSONObject itemsObj = itemsArr.getJSONObject(0);
                JSONObject volumeInfo = itemsObj.getJSONObject("volumeInfo");
                JSONArray authorArr = volumeInfo.getJSONArray("authors");
                String author = authorArr.get(0).toString();
                String title = volumeInfo.getString("title");
                String description = volumeInfo.getString("description");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnail = imageLinks.getString("thumbnail");
                JSONArray categoriesArr = volumeInfo.getJSONArray("categories");
                String category = categoriesArr.get(0).toString();
                System.out.println(category);
                String publisher = volumeInfo.getString("publisher");

    
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
            }
        }
        
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }
}
