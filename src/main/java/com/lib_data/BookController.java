package com.lib_data;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BookController {
    private BookService bookService;
    private BookRepository bookRepo;

    @Autowired
    public BookController(BookService bookService, BookRepository bookRepo){
        this.bookService = bookService;
        this.bookRepo = bookRepo;
    }

    @RequestMapping(value = "/api/deleteBook", method = RequestMethod.DELETE)
    public @ResponseBody String deleteBook(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws Exception {
        String isbn = (String) payload.get("isbn");
        if (bookRepo.findByIsbn(isbn) != null && !(isbn.isEmpty() || isbn == null)){
            try {
                bookRepo.deleteByIsbn(isbn);
                return "Deleted book with isbn: " + isbn;
            } catch (Exception e) {
                System.out.println(e);
                return "Could not delete book with isbn: " + isbn; 
            }
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/api/addBook", method = RequestMethod.POST)
    public @ResponseBody String addIsbn(@RequestParam(value = "isbn") String isbn, HttpServletRequest request) throws Exception {
        if (!(isbn.isEmpty() || isbn.equals(null))){

            if (bookRepo.findByIsbn(isbn) == null){
                bookService.addBook(isbn);
                return "Added Book!";
            }else{
                return "This book already exists";
            }

        }else{
            return null;
        }
    }

    @RequestMapping(value = "/api/addCopy", method = RequestMethod.POST)
    public @ResponseBody String addCopy(@RequestParam(value = "isbn") String isbn, HttpServletRequest request) throws Exception {
        if (!(isbn.isEmpty() || isbn.equals(null))){
            if(bookRepo.findByIsbn(isbn) != null){
                bookService.addCopy(isbn);
                return "Added copy";
            }else{

                return "Could not add copy";
            }
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/api/books", method = RequestMethod.GET)
    public @ResponseBody List<Book> getBooks(HttpServletRequest request) {
        List<Book> book;
        try{
            book = bookService.findAll();
            return book;
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @RequestMapping(value = "/api/markRead", method = RequestMethod.POST)
    public @ResponseBody String markAsRead(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws Exception {
        String isbn = (String) payload.get("isbn");
        Integer read = (Integer) payload.get("read");

        if (!(isbn.isEmpty() || isbn.equals(null))){
            try {
                bookService.markAsRead(isbn, read);
                return "Marked read as " + read.toString();
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }
}
