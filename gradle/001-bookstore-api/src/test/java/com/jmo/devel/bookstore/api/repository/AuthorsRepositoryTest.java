package com.jmo.devel.bookstore.api.repository;

import com.jmo.devel.bookstore.api.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorsRepositoryTest {

    @Autowired
    private AuthorsRepository repository;

    @Test
    void givenName_whenFindByName_thenAuthorsAreReturned(){
        repository.save(
            Author.builder()
                .name( "Jorge Luis" )
                .surnames( "Borges" )
                .build()
        );
        List<Author> authors = repository.findByName( "Jorge Luis" );
        assertThat( authors )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .anyMatch( a -> a.getSurnames().equals( "Borges" ) );
    }

    @Test
    void givenNameWithSpecialCharacters_whenFindByName_thenAuthorsAreReturned(){
        repository.save(
            Author.builder()
                .name( "Ramón María" )
                .surnames( "Del Valle Inclán" )
                .build()
        );
        List<Author> authors = repository.findByName( "Ramón María" );
        assertThat( authors )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .anyMatch( a -> a.getName().equals( "Ramón María" ) );
    }


    @Test
    void givenSurnames_whenFindBySurnames_thenAuthorIsReturned(){
        repository.save(
            Author.builder()
                .name( "Jorge Luis" )
                .surnames( "Borges" )
                .build()
        );
        List<Author> authors = repository.findBySurnames( "Borges" );
        assertThat( authors )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .anyMatch( a -> a.getName().equals( "Jorge Luis" ) );
    }

    @Test
    void givenSurnamesInUppercase_whenFindBySurnamesContainingIgnoreCase_thenAuthorIsReturned(){
        repository.save(
            Author.builder()
                .name( "Ramón María" )
                .surnames( "Del Valle Inclán" )
                .build()
        );
        List<Author> authors = repository.findBySurnamesContainingIgnoreCase( "VALLE" );
        assertThat( authors )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .anyMatch( a -> a.getSurnames().equals( "Del Valle Inclán" ) );
    }

    @Test
    void givenNameAndSurnamesInUppercase_whenFindByNameAndSurnames_thenAuthorIsReturned(){
        repository.save(
            Author.builder()
                .name( "Ramón María" )
                .surnames( "Del Valle Inclán" )
                .build()
        );
        Optional<Author> author = repository
            .findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase(
                "RAMÓN", "INCLÁN"
            );
        assertThat( author )
            .isPresent()
            .get()
                .hasFieldOrPropertyWithValue( "name", "Ramón María" )
                .hasFieldOrPropertyWithValue( "surnames", "Del Valle Inclán" );
    }

    @Test
    void givenAuthors_whenFindByDeathIsNull_thenOnlyOneIsReturned(){
        repository.save(
            Author.builder()
                .name( "Juan" )
                .surnames( "Gómez-Jurado" )
                .birthDate( LocalDate.of( 1977, Month.DECEMBER, 16 ) )
                .build()
        );
        List<Author> alive = repository.findByDeathIsNull();
        assertThat( alive )
            .allMatch( a -> a.getDeath() == null )
            .anyMatch( a -> a.getSurnames().equals( "Gómez-Jurado" ) );
    }

    @Test
    void givenAuthors_whenFindByBirthDateAfter_thenAtLeastOneIsReturned(){
        repository.save(
                Author.builder()
                        .name( "Juan" )
                        .surnames( "Gómez-Jurado" )
                        .birthDate( LocalDate.of( 1977, Month.DECEMBER, 16 ) )
                        .build()
        );
        List<Author> alive = repository.findByBirthDateAfter( LocalDate.of( 1970, Month.JANUARY, 1 ) );
        assertThat( alive )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .allMatch( a -> a.getBirthDate().getYear() > 1970 );
    }

}
