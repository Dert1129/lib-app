package com.lib_data;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer>{

    List<Book> findAll();
    
    @SuppressWarnings("unchecked")
    public Book save(Book book);

    public Book findByIsbn(String Isbn);

    @Query(value = "select * from Library.books where books.id= :id", nativeQuery = true)
    public Book findBookById(@Param("id") Integer id);

    @Transactional
    public void deleteById(int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Library.books SET books.is_read = :isRead where id = :id", nativeQuery = true)
    public void setBookAsRead(@Param("id") Integer id, @Param("isRead") Integer isRead);

    // @Transactional
    // public void deleteByIsbn(String isbn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Library.books SET books.copies = :copies where id = :id", nativeQuery = true)
    public void addCopies(@Param("id") Integer id, @Param("copies") Integer copies);

    @Query(value = "select copies from Library.books where id= :id", nativeQuery = true)
    public Integer getCopiesById(@Param ("id") Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Library.books SET books.title = :title, books.genre = :genreList, books.author_name = :authorName, books.category = :category, books.publisher = :publisher, books.copies = :copies, books.start_date = :startDate, books.end_date = :endDate WHERE id = :id", nativeQuery = true)
    public void updateBookById(@Param("title") String title, @Param("genreList") List<String> genreList, @Param("authorName") String authorName, @Param("category") String category, @Param("publisher") String pubisher, @Param("copies") Integer copies, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("id") Integer id);
}
