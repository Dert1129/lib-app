package com.lib_data;

import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.util.ArrayList;
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

    @RequestMapping(value = "/api/editBook", method =  RequestMethod.POST)
    public @ResponseBody String editBook(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws Exception {
        String title = payload.get("title").toString();
        String category =  payload.get("category").toString();
        Integer id = (Integer) payload.get("id");
        String publisher = payload.get("publisher").toString();
        @SuppressWarnings("unchecked")
        List<String> genreList = (List<String>) payload.get("genre");
        String authorName = payload.get("authorName").toString();
        String startDateString = payload.get("startDate").toString();
        String endDateString = payload.get("endDate").toString();
        Date startDate = Date.valueOf(startDateString);
        Date endDate = Date.valueOf(endDateString);
        Integer copies = (Integer) payload.get("copies");
        if (bookRepo.findBookById(id) != null){
            bookService.editBook(id, category, title, authorName, publisher, genreList, copies, startDate, endDate);
            return "Updated book";
        }else{
            return "Book could not be updated";
        }

    }

    @RequestMapping(value = "/api/getBook", method =  RequestMethod.GET)
    public @ResponseBody Book getBook(@RequestParam(value = "id") Integer id, HttpServletRequest request) throws Exception {
        try{
            return bookService.findBookById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    @RequestMapping(value = "/api/addManual", method =  RequestMethod.POST)
    public @ResponseBody String addManual(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws Exception {
        System.out.println(payload);
        String title = payload.get("title").toString();
        String category =  payload.get("category").toString();
        String isbn = payload.get("isbn").toString();
        String publisher = payload.get("publisher").toString();
        String imageLink = payload.get("imageLink").toString();
        String description = payload.get("description").toString();
        @SuppressWarnings("unchecked")
        List<String> genreList = (List<String>) payload.get("genre");
        String author = payload.get("authorName").toString();
        Integer copies = (Integer) payload.get("copies");
        if (bookRepo.findByIsbn(isbn) == null){
            bookService.addManual(isbn, category, title, publisher, author, genreList, copies, imageLink, description);
            return "Added book manually";
        }else{
            return "Book exists in catalog";
        }

    }



    @RequestMapping(value = "/api/deleteBook", method = RequestMethod.DELETE)
    public @ResponseBody String deleteBook(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws Exception {
        Integer id = (Integer) payload.get("id");
        if (bookRepo.findBookById(id) != null && !(id == null)){
            try {
                bookRepo.deleteById(id);
                return "Deleted book with isbn: " + id;
            } catch (Exception e) {
                System.out.println(e);
                return "Could not delete book with isbn: " + id; 
            }
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/api/getBook", method = RequestMethod.POST)
    public @ResponseBody Book addIsbn(@RequestParam(value = "isbn") String isbn, HttpServletRequest request) throws Exception {
        if (!(isbn.isEmpty() || isbn.equals(null))){

            if (bookRepo.findByIsbn(isbn) == null){
                Book response = bookService.getBook(isbn);
                System.out.println(response);
                if (response != null){
                    return response;
                }else{
                    return null;
                }
            }else{
                return null;
            }

        }else{
            return null;
        }
    }

    @RequestMapping(value = "/api/addCopy", method = RequestMethod.POST)
    public @ResponseBody String addCopy(@RequestParam(value = "isbn") Integer id, HttpServletRequest request) throws Exception {
        if (id != null){
            if(bookRepo.findBookById(id) != null){
                bookService.addCopy(id);
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
        Integer id = (Integer) payload.get("id");
        Integer read = (Integer) payload.get("read");

        if (id != null){
            try {
                bookService.markAsRead(id, read);
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
