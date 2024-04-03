package com.jmo.devel.bookstore.api.service;

import com.jmo.devel.bookstore.api.exception.ExistingPublisherException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.exception.PublisherNotFoundException;
import com.jmo.devel.bookstore.api.model.Publisher;
import com.jmo.devel.bookstore.api.repository.PublishersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(classes = {PublishersService.class})
class PublishersServiceTest {

    @Autowired
    private PublishersService service;

    @MockBean
    private PublishersRepository repository;

    @Test
    void givenAnExistingPublisher_whenGetById_thenPublisherIsReturned(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of(
                Publisher.builder().id( 1L ).build()
            ));
        assertThat( service.getById( 1L ) )
            .hasFieldOrPropertyWithValue( "id", 1L );
    }

    @Test
    void givenANotExistingPublisher_whenGetById_thenPublisherNotFoundIsThrown(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( PublisherNotFoundException.class )
            .isThrownBy( () -> service.getById( 1L ) )
            .withMessageStartingWith( "publisher not found" );
    }

    @Test
    void givenAnExistingName_whenGetByName_thenPublisherIsReturned(){
        Mockito.when( repository.findByName( "Planeta" ) )
            .thenReturn( Optional.of(
                Publisher.builder().name( "Planeta" ).build()
            ));
        assertThat( service.getByName( "Planeta" ) )
            .hasFieldOrPropertyWithValue( "name", "Planeta" );
    }

    @Test
    void givenANotExistingName_whenGetByName_thenPublisherNotFoundIsThrown(){
        Mockito.when( repository.findByName( "Planeta" ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( PublisherNotFoundException.class )
            .isThrownBy( () -> service.getByName( "Planeta" ) )
            .withMessageStartingWith( "publisher not found" );
    }

    @Test
    void givenTwoPublishers_whenGetByHeadquarters_thenPublishersAreReturned(){
        Mockito.when( repository.findByHeadquartersIn( List.of( "Barcelona" ) ) )
            .thenReturn(
                List.of(
                    Publisher.builder()
                        .name( "Planeta" )
                        .headquarters( "Barcelona" )
                        .website( "https://www.planeta.es/es" )
                        .build(),
                    Publisher.builder()
                        .name( "Blackie Books" )
                        .headquarters( "Barcelona" )
                        .website( "https://blackiebooks.org/" )
                        .build()
                )
            );
        List<Publisher> publishers = service.getByHeadquarters( List.of("Barcelona") );
        assertThat( publishers )
            .hasSizeGreaterThanOrEqualTo( 2 )
            .allMatch( p -> p.getHeadquarters().equals( "Barcelona" ) );
    }

    @Test
    void givenANotExistingHeadquarters_whenGetByHeadquarters_thenNoResultsIsThrown(){
        Mockito.when( repository.findByHeadquartersIn( List.of( "Barcelona" ) ) )
            .thenReturn( List.of() );
        List<String> headquarters = List.of( "Barcelona" );
        assertThatExceptionOfType( NoResultsException.class )
            .isThrownBy( () -> service.getByHeadquarters( headquarters ) )
            .withMessageStartingWith( "no results for" );
    }

    @Test
    void givenANotExistingPublisher_whenCreate_thenPublisherIsCreated(){
        Publisher b = Publisher.builder()
            .id( 1L ).name( "Planeta" ).build();
        Mockito.when( repository.findByName( "Planeta" ) )
            .thenReturn( Optional.empty() );
        Mockito.when( repository.save( b ) ).thenReturn( b );
        assertThat( service.create( b ) )
            .hasFieldOrPropertyWithValue( "name", "Planeta" );
    }

    @Test
    void givenAnExistingPublisher_whenCreate_thenExistingPublisherIsThrown(){
        Publisher b = Publisher.builder()
            .id( 1L ).name( "Planeta" ).build();
        Mockito.when( repository.findByName( "Planeta" ) )
            .thenReturn( Optional.of( b ) );
        assertThatExceptionOfType( ExistingPublisherException.class )
            .isThrownBy( () -> service.create( b ) )
            .withMessageEndingWith( "already exists" );
    }

    @Test
    void givenAnExistingPublisher_whenUpdate_thenPublisherIsUpdated(){
        Publisher p = Publisher.builder()
            .id( 1L ).name( "Planeta" ).build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of( p ) );
        Mockito.when( repository.save( p ) ).thenReturn( p );
        assertThat( service.update( 1L, p ) )
            .hasFieldOrPropertyWithValue( "id", 1L );
    }

    @Test
    void givenANotExistingPublisher_whenUpdate_thenPublisherNotFoundIsThrown(){
        Publisher p = Publisher.builder()
            .id( 1L ).name( "Planeta" ).build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( PublisherNotFoundException.class )
            .isThrownBy( () -> service.update( 1L, p ) )
            .withMessageStartingWith( "publisher not found" );
    }

    @Test
    void givenAPublisher_whenDelete_thenRepositoryDeleteIsCalled(){
        Publisher p = Publisher.builder().id( 1L ).build();
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.of( p ) );
        service.delete( 1L );
        Mockito.verify( repository,
            Mockito.times( 1 ) ).deleteById( 1L );
    }

    @Test
    void givenANotExistingPublisher_whenDelete_thenPublisherNotFoundIsThrown(){
        Mockito.when( repository.findById( 1L ) )
            .thenReturn( Optional.empty() );
        assertThatExceptionOfType( PublisherNotFoundException.class )
            .isThrownBy( () -> service.delete( 1L ) )
            .withMessageStartingWith( "publisher not found" );
    }

}
