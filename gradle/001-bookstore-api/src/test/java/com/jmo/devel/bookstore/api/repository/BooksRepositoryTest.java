package com.jmo.devel.bookstore.api.repository;

import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.model.Book;
import com.jmo.devel.bookstore.api.model.Publisher;
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
class BooksRepositoryTest {

    @Autowired
    private BooksRepository repository;
    @Autowired
    private AuthorsRepository authors;
    @Autowired
    private PublishersRepository publishers;

    @Test
    void givenATitle_whenFindByTitle_thenBookIsReturned(){
        repository.save(
            Book.builder().title( "Reina Roja" ).build()
        );
        Optional<Book> book = repository.findByTitle( "Reina Roja" );
        assertThat( book )
            .isPresent()
            .get().hasFieldOrPropertyWithValue( "title", "Reina Roja" );
    }

    @Test
    void givenAPartialUppercaseTitle_whenFindByTitleContainingIgnoreCase_thenAtLeastBookIsReturned(){
        repository.save(
            Book.builder().title( "Luces de bohemia" ).build()
        );
        List<Book> books = repository.findByTitleContainingIgnoreCase( "BOHEMIA" );
        assertThat( books )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .anyMatch( b -> b.getTitle().equals( "Luces de bohemia" ) );
    }

    @Test
    void givenAnIsbn_whenFindByIsbn_thenBookIsReturned(){
        repository.save(
            Book.builder().title( "Sin noticias de Gurb" ).isbn( "9788432221255" ).build()
        );
        Optional<Book> book = repository.findByIsbn( "9788432221255" );
        assertThat( book )
            .isPresent()
            .get().hasFieldOrPropertyWithValue( "isbn", "9788432221255" );
    }

    @Test
    void givenARangeOfPages_whenFindByPagesBetween_thenBooksAreReturned(){
        repository.save(
            Book.builder().title( "El arte de la guerra" ).pages( 98 ).build()
        );
        List<Book> books = repository.findByPagesBetween( 0, 200 );
        assertThat( books )
            .allMatch( b -> b.getPages() >= 0 && b.getPages() <= 200 )
            .anyMatch( b -> b.getTitle().equals( "El arte de la guerra" ) );
    }

    @Test
    void givenAYear_whenFindByPublishedYear_thenBooksAreReturned(){
        repository.saveAll(
            List.of(
                Book.builder()
                    .title( "De la estupidez a la locura" )
                    .published( LocalDate.of( 2016, Month.FEBRUARY, 26 ) )
                    .build(),
                Book.builder()
                    .title( "Patria" )
                    .published( LocalDate.of( 2016, Month.SEPTEMBER, 6 ) )
                    .build()
            )
        );
        List<Book> books = repository.findByPublishedYear( 2016 );
        assertThat( books )
            .allMatch( b -> b.getPublished().getYear() == 2016 )
            .anyMatch( b -> b.getTitle().equals( "De la estupidez a la locura" ) )
            .anyMatch( b -> b.getTitle().equals( "Patria" ) );
    }

    @Test
    void givenAPublisherName_whenFindByPublisherName_thenBooksAreReturned(){

        publishers.save(
            Publisher.builder().name( "Blackie Books" ).build()
        );
        Optional<Publisher> publisher = publishers.findByName( "Blackie Books" );
        assertThat( publisher ).isPresent();

        Publisher p = publisher.get();
        repository.saveAll(
            List.of(
                Book.builder().publisher( p ).title( "Calypso" ).build(),
                Book.builder().publisher( p ).title( "Los números de la vida" ).build()
            )
        );
        List<Book> books = repository.findByPublisherName( "Blackie Books" );
        assertThat( books ).allMatch( b -> b.getPublisher().getName().equals( "Blackie Books" ) )
            .anyMatch( b -> b.getTitle().equals( "Calypso" ) )
            .anyMatch( b -> b.getTitle().equals( "Los números de la vida" ) );

    }

    @Test
    void givenAuthor_whenFindByAuthorNameAndAuthorSurnames_thenBooksAreReturned(){

        authors.save(
            Author.builder().name( "Federico" ).surnames( "García Lorca" ).build()
        );
        Optional<Author> author = authors.findBySurnames( "García Lorca" );
        assertThat( author ).isPresent();

        Author a = author.get();
        repository.saveAll(
            List.of(
                Book.builder().author( a ).title( "Bodas de sangre" ).build(),
                Book.builder().author( a ).title( "La casa de Bernarda Alba" ).build(),
                Book.builder().author( a ).title( "Yerma" ).build()
            )
        );

        List<Book> books = repository.findByAuthorNameAndAuthorSurnames( "Federico", "García Lorca" );
        assertThat( books )
            .allMatch(
                b -> b.getAuthor().getName().equals( "Federico" ) &&
                b.getAuthor().getSurnames().equals( "García Lorca" )
            )
            .anyMatch( b -> b.getTitle().equals( "Yerma" ) )
            .anyMatch( b -> b.getTitle().equals( "Bodas de sangre" ) )
            .anyMatch( b -> b.getTitle().equals( "La casa de Bernarda Alba" ) );
    }

    @Test
    void givenAnIsbn_whenDeleteByIsbn_thenBookIsDeleted(){
        repository.save(
            Book.builder().title( "Sin noticias de Gurb" ).isbn( "9788432221255" ).build()
        );
        assertThat( repository.findByIsbn( "9788432221255" ) ).isPresent();
        repository.deleteByIsbn( "9788432221255" );
        assertThat( repository.findByIsbn( "9788432221255" ) ).isNotPresent();
    }

}
