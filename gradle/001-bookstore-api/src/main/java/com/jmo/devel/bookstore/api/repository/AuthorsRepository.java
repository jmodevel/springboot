package com.jmo.devel.bookstore.api.repository;

import com.jmo.devel.bookstore.api.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorsRepository extends CrudRepository<Author, Long> {

    List<Author> findByName( String name );
    List<Author> findBySurnames( String surnames );
    List<Author> findBySurnamesContainingIgnoreCase( String surname );
    Optional<Author> findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase( String name, String surnames );
    List<Author> findByDeathIsNull();
    List<Author> findByBirthDateAfter( LocalDate date );


}
