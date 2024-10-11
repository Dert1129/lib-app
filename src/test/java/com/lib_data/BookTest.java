package com.lib_data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class BookTest {
    private Book book;
    @BeforeEach
    public void setUp() throws Exception {
        book = new Book();
    }

    @AfterEach
    public void tearDown() throws Exception {
        book = null;
    }

    @Test
    public void testSetId() {
        book.setId(0);
        assertEquals(0, book.getId());
    }

    @Test
    public void testSetTitle() {
        book.setTitle("The Stand");
        assertEquals("The Stand", book.getTitle());
    }

    @Test
    public void testSetAuthorName() {
        book.setAuthorName("Stephen");
        assertEquals("Stephen", book.getAuthorName());
    }

    @Test
    public void testSetIsbn() {
        book.setIsbn("123456789");
        assertEquals("123456789", book.getIsbn());
    }

    @Test
    public void testSetGenre() {
        book.setGenre("horror");
        assertEquals("horror", book.getGenre());
    }

    @Test
    public void testSetCopies(){
        book.setCopies(2);
        assertEquals(2, book.getCopies());
    }
}
