package com.jmo.devel.bookstore.api.repository;

import com.jmo.devel.bookstore.api.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByTitle( String title );
    List<Book> findByTitleContainingIgnoreCase( String title );
    Optional<Book> findByIsbn( String isbn );
    List<Book> findByPagesBetween( int min, int max );

    @Query( "select b from Book b where year(published) = :year")
    List<Book> findByPublishedYear( int year );

    List<Book> findByPublisherName( String publisher );
    List<Book> findByAuthorNameAndAuthorSurnames( String name, String surnames );

    void deleteByIsbn( String isbn );
}
