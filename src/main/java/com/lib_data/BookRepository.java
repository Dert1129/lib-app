package com.lib_data;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer>{

    List<Book> findAll();
    
    @SuppressWarnings("unchecked")
    public Book save(Book book);

    public Book findByIsbn(String Isbn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Library.books SET books.is_read = :isRead where isbn = :isbn", nativeQuery = true)
    public void setBookAsRead(@Param("isbn") String isbn, @Param("isRead") Integer isRead);

    @Transactional
    public void deleteByIsbn(String isbn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Library.books SET books.copies = :copies where isbn = :isbn", nativeQuery = true)
    public void addCopies(@Param("isbn") String isbn, @Param("copies") Integer copies);

    @Query(value = "select copies from Library.books where isbn= :isbn", nativeQuery = true)
    public Integer getCopiesByIsbn(@Param ("isbn") String isbn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Library.books SET books.title = :title, books.genre = :genre, books.author_name = :authorName, books.category = :category, books.publisher = :publisher, books.copies = :copies WHERE isbn = :isbn", nativeQuery = true)
    public void updateBookByIsbn(@Param("title") String title, @Param("genre") String genre, @Param("authorName") String authorName, @Param("category") String category, @Param("publisher") String pubisher, @Param("copies") Integer copies, @Param("isbn") String isbn);
}
