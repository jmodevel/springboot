package com.jmo.devel.bookstore.api.repository;

import com.jmo.devel.bookstore.api.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorsRepository extends CrudRepository<Author, Long> {

    Optional<Author> findByName( String name );
    Optional<Author> findBySurnames( String surnames );
    Optional<Author> findBySurnamesContainingIgnoreCase( String surname );
    List<Author> findByDeathIsNull();
    List<Author> findByBirthDateAfter( LocalDate date );


}
