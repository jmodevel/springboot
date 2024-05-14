package com.jmo.devel.bookstore.api.service;

import com.jmo.devel.bookstore.api.exception.BookNotFoundException;
import com.jmo.devel.bookstore.api.exception.ExistingBookException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.model.Book;
import com.jmo.devel.bookstore.api.model.Publisher;
import com.jmo.devel.bookstore.api.repository.AuthorsRepository;
import com.jmo.devel.bookstore.api.repository.BooksRepository;
import com.jmo.devel.bookstore.api.repository.PublishersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(classes = {BooksService.class})
class BooksServiceTest {

    @Autowired
    private BooksService service;

    @MockBean
    private BooksRepository repository;

    @MockBean
    private AuthorsRepository authorsRepository;

    @MockBean
    private PublishersRepository publishersRepository;

    @Test
    void givenAnExistingBook_whenGetById_thenBookIsReturned(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of(
                Book.builder().id( 1L ).build()
            ));
        assertThat( service.getById( 1L ) )
            .hasFieldOrPropertyWithValue( "id", 1L );
    }

    @Test
    void givenANotExistingBook_whenGetById_thenBookNotFoundIsThrown(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( BookNotFoundException.class )
            .isThrownBy( () -> service.getById( 1L ) )
            .withMessageStartingWith( "book not found" );
    }

    @Test
    void givenAnExistingTitle_whenGetByTitle_thenBookIsReturned(){
        Mockito.when( repository.findByTitle( "Reina Roja" ) )
            .thenReturn( Optional.of(
                Book.builder().title( "Reina Roja" ).build()
            ));
        assertThat( service.getByTitle( "Reina Roja" ) )
            .hasFieldOrPropertyWithValue( "title", "Reina Roja" );
    }

    @Test
    void givenANotExistingTitle_whenGetByTitle_thenBookNotFoundIsThrown(){
        Mockito.when( repository.findByTitle( "Reina Roja" ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( BookNotFoundException.class )
            .isThrownBy( () -> service.getByTitle( "Reina Roja" ) )
            .withMessageStartingWith( "book not found" );
    }

    @Test
    void givenAnExistingPartialTitle_whenGetByTitleLike_thenBooksAreReturned(){
        Mockito.when( repository.findByTitleContainingIgnoreCase( "BOHEMIA" ) )
            .thenReturn( List.of(
                Book.builder().title( "Luces de bohemia" ).build()
            ));
        assertThat( service.getByTitleLike( "BOHEMIA" ) )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .allMatch( p -> p.getTitle().toUpperCase().contains( "BOHEMIA" ) );
    }

    @Test
    void givenANotExistingTitle_whenGetByTitle_thenNoResultsIsThrown(){
        Mockito.when( repository.findByTitleContainingIgnoreCase( "BOHEMIA" ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByTitleLike( "BOHEMIA" ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenAnExistingIsbn_whenGetByTitleLike_thenBookIsReturned(){
        Mockito.when( repository.findByIsbn( "9788432221255" ) )
            .thenReturn( Optional.of(
                Book.builder().isbn( "9788432221255" ).build()
            ));
        assertThat( service.getByIsbn( "9788432221255" ) )
            .hasFieldOrPropertyWithValue( "isbn", "9788432221255" );
    }

    @Test
    void givenANotExistingIsbn_whenGetByIsbn_thenBookNotFoundIsThrown(){
        Mockito.when( repository.findByIsbn( "9788432221255" ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( BookNotFoundException.class )
            .isThrownBy( () -> service.getByIsbn( "9788432221255" ) )
            .withMessageStartingWith( "book not found" );
    }

    @Test
    void givenAnExistingPagesRange_whenGetByPages_thenBooksAreReturned(){
        Mockito.when( repository.findByPagesBetween( 0, 100 ) )
            .thenReturn( List.of(
                Book.builder().title( "El arte de la guerra" ).pages( 98 ).build()
            ));
        assertThat( service.getByPages( 0, 100 ) )
            .hasSize( 1 )
            .allMatch( p -> p.getPages() >= 0 && p.getPages() <= 100 );
    }

    @Test
    void givenANotExistingPagesRange_whenGetByPages_thenNoResultsIsThrown(){
        Mockito.when( repository.findByPagesBetween( 0, 100 ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByPages( 0, 100 ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenAnExistingYear_whenGetByYear_thenBooksAreReturned(){
        Mockito.when( repository.findByPublishedYear( 2016 ) )
            .thenReturn( List.of(
                Book.builder()
                    .title( "De la estupidez a la locura" )
                    .published( LocalDate.of( 2016, Month.FEBRUARY, 26 ) )
                    .build(),
                Book.builder()
                    .title( "Patria" )
                    .published( LocalDate.of( 2016, Month.SEPTEMBER, 6 ) )
                    .build()
            ));
        assertThat( service.getByYear( 2016 ) )
            .hasSize( 2 )
            .allMatch( p -> p.getPublished().getYear() == 2016 );
    }

    @Test
    void givenANotExistingYear_whenGetByYear_thenNoResultsIsThrown(){
        Mockito.when( repository.findByPublishedYear( 2016 ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByYear( 2016 ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenAnExistingPublisher_whenGetByPublisher_thenBooksAreReturned(){
        Publisher p = Publisher.builder().name( "Blackie Books" ).build();
        Mockito.when( repository.findByPublisherName( "Blackie Books" ) )
            .thenReturn( List.of(
                Book.builder().publisher( p ).title( "Calypso" ).build(),
                Book.builder().publisher( p ).title( "Los números de la vida" ).build()
            ));
        assertThat( service.getByPublisher( "Blackie Books" ) )
            .hasSize( 2 )
            .allMatch( b -> b.getPublisher().getName().equals( "Blackie Books" ) );
    }

    @Test
    void givenANotExistingPublisher_whenGetByPublisher_thenNoResultsIsThrown(){
        Mockito.when( repository.findByPublisherName( "Blackie Books" ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByPublisher( "Blackie Books" ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenAnExistingAuthor_whenGetByAuthor_thenBooksAreReturned(){
        Author a = Author.builder().name( "Federico" ).surnames( "García Lorca" ).build();
        Mockito.when( repository.findByAuthorNameAndAuthorSurnames( "Federico", "García Lorca" ) )
            .thenReturn( List.of(
                Book.builder().author( a ).title( "Bodas de sangre" ).build(),
                Book.builder().author( a ).title( "La casa de Bernarda Alba" ).build(),
                Book.builder().author( a ).title( "Yerma" ).build()
            ));
        assertThat( service.getByAuthor( "Federico", "García Lorca" ) )
            .hasSize( 3 )
            .allMatch(
                b -> b.getAuthor().getName().equals( "Federico" ) &&
                    b.getAuthor().getSurnames().equals( "García Lorca" )
            )
            .anyMatch( b -> b.getTitle().equals( "Yerma" ) )
            .anyMatch( b -> b.getTitle().equals( "Bodas de sangre" ) )
            .anyMatch( b -> b.getTitle().equals( "La casa de Bernarda Alba" ) );
    }

    @Test
    void givenANotExistingAuthor_whenGetByAuthor_thenNoResultsIsThrown(){
        Mockito.when( repository.findByAuthorNameAndAuthorSurnames( "Federico", "García Lorca" ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByAuthor( "Federico", "García Lorca" ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenANotExistingBook_whenCreate_thenBookIsCreated(){
        Book b = Book.builder()
            .id( 1L ).isbn( "9788432221255" ).build();
        Mockito.when( repository.findByIsbn( "9788432221255" ) )
            .thenReturn( Optional.empty() );
        Mockito.when( repository.save( b ) ).thenReturn( b );
        assertThat( service.create( b ) )
            .hasFieldOrPropertyWithValue( "isbn", "9788432221255" );
    }

    @Test
    void givenAnExistingBook_whenCreate_thenExistingBookIsThrown(){
        Book b = Book.builder()
            .id( 1L ).isbn( "9788432221255" ).build();
        Mockito.when( repository.findByIsbn( "9788432221255" ) )
            .thenReturn( Optional.of( b ) );
        assertThatExceptionOfType( ExistingBookException.class )
            .isThrownBy( () -> service.create( b ) )
            .withMessageEndingWith( "already exists" );
    }

    @Test
    void givenAnExistingBook_whenUpdate_thenBookIsUpdated(){
        Book b = Book.builder()
            .isbn( "9788432221255" )
            .build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of( b ) );
        Mockito.when( repository.save( b ) ).thenReturn( b );
        assertThat( service.update( 1L, b ) )
            .hasFieldOrPropertyWithValue( "id", 1L );
    }

    @Test
    void givenANotExistingBook_whenUpdate_thenBookNotFoundIsThrown(){
        Book b = Book.builder()
            .id( 1L )
            .isbn( "9788432221255" )
            .build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( BookNotFoundException.class )
            .isThrownBy( () -> service.update( 1L, b ) )
            .withMessageStartingWith( "book not found" );
    }

    @Test
    void givenABook_whenDelete_thenRepositoryDeleteIsCalled(){
        Book b = Book.builder().id( 1L ).build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of( b ) );
        service.delete( 1L );
        Mockito.verify( repository,
            Mockito.times( 1 ) ).deleteById( 1L );
    }

    @Test
    void givenANotExistingBook_whenDelete_thenBookNotFoundIsThrown(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( BookNotFoundException.class )
            .isThrownBy( () -> service.delete( 1L ) )
            .withMessageStartingWith( "book not found" );
    }

}
