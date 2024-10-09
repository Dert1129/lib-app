package com.lib_data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @RequestMapping(value = "/api/addBook", method = RequestMethod.POST)
    public @ResponseBody String addIsbn(@RequestParam(value = "isbn") String isbn, HttpServletRequest request) throws Exception {
        if (!(isbn.isEmpty() || isbn.equals(null))){

            bookService.addBook(isbn);
            return "Added book!";
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
}
