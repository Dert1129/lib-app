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
}
