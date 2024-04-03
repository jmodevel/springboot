package com.jmo.devel.bookstore.api.service;

import com.jmo.devel.bookstore.api.exception.AuthorNotFoundException;
import com.jmo.devel.bookstore.api.exception.ExistingAuthorException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.repository.AuthorsRepository;
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

@SpringBootTest(classes = {AuthorsService.class})
class AuthorsServiceTest {

    @Autowired
    private AuthorsService service;

    @MockBean
    private AuthorsRepository repository;

    @Test
    void givenAnExistingAuthor_whenGetById_thenAuthorIsReturned(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of(
                Author.builder().id(1L).build()
            ));
        assertThat( service.getById( 1L ) )
            .hasFieldOrPropertyWithValue( "id", 1L );
    }

    @Test
    void givenANotExistingAuthor_whenGetById_thenAuthorNotFoundIsThrown(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( AuthorNotFoundException.class )
            .isThrownBy( () -> service.getById( 1L ) )
            .withMessageStartingWith( "author not found" );
    }

    @Test
    void givenAnExistingAuthor_whenGetByNameAndSurnames_thenAuthorIsReturned(){
        Mockito.when( repository.findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase(
            "Ramón", "Valle"
            ))
            .thenReturn( Optional.of(
                Author.builder()
                    .name( "Ramón María" )
                    .surnames( "Del Valle Inclán" )
                    .build()
            ));
        assertThat( service.getByNameAndSurnames( "Ramón", "Valle" ) )
            .hasFieldOrPropertyWithValue( "name", "Ramón María" )
            .hasFieldOrPropertyWithValue( "surnames", "Del Valle Inclán" );
    }

    @Test
    void givenANotExistingAuthor_whenGetByNameAndSurnames_thenAuthorNotFoundIsThrown(){
        Mockito.when( repository.findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase(
            "Ramón", "Valle"
            ))
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( AuthorNotFoundException.class )
            .isThrownBy( () -> service.getByNameAndSurnames( "Ramón", "Valle" ) )
            .withMessageStartingWith( "author not found" );
    }

    @Test
    void givenAName_whenGetByName_thenAuthorsAreReturned(){
        Mockito.when( repository.findByName( "Jorge Luis" ) )
            .thenReturn( List.of(
                Author.builder().name( "Jorge Luis" ).build()
            ));
        assertThat( service.getByName( "Jorge Luis" ) )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .allMatch( a-> a.getName().equals( "Jorge Luis" ) );
    }

    @Test
    void givenANotExistingName_whenGetByName_thenNoResultsIsThrown(){
        Mockito.when( repository.findByName( "Jorge Luis" ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByName( "Jorge Luis" ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenASurnames_whenGetBySurnames_thenAuthorsAreReturned(){
        Mockito.when( repository.findBySurnames( "Highsmith" ) )
            .thenReturn( List.of(
                Author.builder().surnames( "Highsmith" ).build()
            ));
        assertThat( service.getBySurnames( "Highsmith" ) )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .allMatch( a -> a.getSurnames().equals( "Highsmith" ) );
    }

    @Test
    void givenANotExistingSurnames_whenGetBySurnames_thenNoResultsIsThrown(){
        Mockito.when( repository.findByName( "Highsmith" ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getBySurnames( "Highsmith" ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenAnUppercaseSurnames_whenGetBySurnamesLike_thenAuthorsAreReturned(){
        Mockito.when( repository.findBySurnamesContainingIgnoreCase( "CHRISTIE" ) )
            .thenReturn( List.of(
                Author.builder().surnames( "Christie" ).build()
            ));
        assertThat( service.getBySurnamesLike( "CHRISTIE" ) )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .allMatch( a -> a.getSurnames().equalsIgnoreCase( "CHRISTIE" ) );
    }

    @Test
    void givenANotExistingSurnames_whenGetBySurnamesLike_thenNoResultsIsThrown(){
        Mockito.when( repository.findBySurnamesContainingIgnoreCase( "CHRISTIE" ) )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getBySurnamesLike( "CHRISTIE" ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenAnAuthor_whenGetAlive_thenAuthorsAreReturned(){
        Mockito.when( repository.findByDeathIsNull() )
            .thenReturn( List.of(
                Author.builder()
                    .name( "Juan" )
                    .surnames( "Gómez-Jurado" )
                    .build()
            ));
        assertThat( service.getAlive() )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .allMatch( a -> a.getDeath() == null );
    }

    @Test
    void givenNoAliveAuthors_whenGetAlive_thenNoResultsIsThrown(){
        Mockito.when( repository.findByDeathIsNull() )
            .thenReturn( List.of() );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getAlive() )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenAnAuthor_whenGetByBirthDateAfter_thenAuthorsAreReturned(){
        Mockito.when( repository.findByBirthDateAfter(
                LocalDate.of( 1970, Month.JANUARY, 1 )
            ))
            .thenReturn( List.of(
                Author.builder()
                    .name( "Juan" )
                    .surnames( "Gómez-Jurado" )
                    .birthDate( LocalDate.of( 1977, Month.DECEMBER, 16 ) )
                    .build()
            ));
        LocalDate date = LocalDate.of(1970, Month.JANUARY, 1);
        assertThat( service.getByBirthDateAfter( date ) )
            .hasSizeGreaterThanOrEqualTo( 1 )
            .allMatch( a -> a.getBirthDate().getYear() > 1970 );
    }

    @Test
    void givenNoAuthorsBornAfterYear_whenGetByBirthDateAfter_thenNoResultsIsThrown(){
        Mockito.when( repository.findByBirthDateAfter(
                LocalDate.of( 1970, Month.JANUARY, 1 )
            ) )
            .thenReturn( List.of() );
        LocalDate date = LocalDate.of(1970, Month.JANUARY, 1);
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByBirthDateAfter( date ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenANotExistingAuthor_whenCreate_thenAuthorIsCreated(){
        Author a = Author.builder()
            .id( 1L )
            .name( "Jorge Luis" )
            .surnames( "Borges" )
            .build();
        Mockito.when(
            repository.findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase(
                "Jorge Luis", "Borges"
            ))
            .thenReturn( Optional.empty() );
        Mockito.when( repository.save( a ) ).thenReturn( a );
        assertThat( service.create( a ) )
            .hasFieldOrPropertyWithValue( "name", "Jorge Luis" )
            .hasFieldOrPropertyWithValue( "surnames", "Borges" );
    }

    @Test
    void givenAnExistingAuthor_whenCreate_thenExistingAuthorIsThrown(){
        Author a = Author.builder()
            .id( 1L )
            .name( "Jorge Luis" )
            .surnames( "Borges" )
            .build();
        Mockito.when( repository.findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase(
                "Jorge Luis", "Borges"
            ))
            .thenReturn( Optional.of( a ) );
        assertThatExceptionOfType( ExistingAuthorException.class )
            .isThrownBy( () -> service.create( a ) )
            .withMessageEndingWith( "already exists" );
    }

    @Test
    void givenAnExistingAuthor_whenUpdate_thenAuthorIsUpdated(){
        Author a = Author.builder()
            .id( 1L )
            .name( "Patricia" )
            .surnames( "Highsmith" )
            .build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of( a ) );
        Mockito.when( repository.save( a ) ).thenReturn( a );
        assertThat( service.update( 1L, a ) )
            .hasFieldOrPropertyWithValue( "id", 1L );
    }

    @Test
    void givenANotExistingAuthor_whenUpdate_thenAuthorNotFoundIsThrown(){
        Author a = Author.builder()
            .id( 1L )
            .name( "Patricia" )
            .surnames( "Highsmith" )
            .build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( AuthorNotFoundException.class )
            .isThrownBy( () -> service.update( 1L, a ) )
            .withMessageStartingWith( "author not found" );
    }

    @Test
    void givenAnAuthor_whenDelete_thenRepositoryDeleteIsCalled(){
        Author a = Author.builder().id( 1L ).build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of( a ) );
        service.delete( 1L );
        Mockito.verify( repository,
            Mockito.times( 1 ) ).deleteById( 1L );
    }

    @Test
    void givenANotExistingAuthor_whenDelete_thenAuthorNotFoundIsThrown(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( AuthorNotFoundException.class )
            .isThrownBy( () -> service.delete( 1L ) )
            .withMessageStartingWith( "author not found" );
    }

}
