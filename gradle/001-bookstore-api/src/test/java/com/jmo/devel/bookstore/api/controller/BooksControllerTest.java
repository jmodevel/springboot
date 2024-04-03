package com.jmo.devel.bookstore.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.api.dto.BookDto;
import com.jmo.devel.bookstore.api.exception.BookNotFoundException;
import com.jmo.devel.bookstore.api.exception.ExistingBookException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.model.Book;
import com.jmo.devel.bookstore.api.model.Publisher;
import com.jmo.devel.bookstore.api.service.BooksService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BooksController.class)
@ExtendWith(SpringExtension.class)
@ComponentScan( basePackages = {
    "com.jmo.devel.bookstore.api.assembler",
    "com.jmo.devel.bookstore.api.configuration",
    "com.jmo.devel.bookstore.api.dto.mapper"
})
class BooksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BooksService service;

    @Test
    void givenAnExistingBook_whenRequestById_thenBookIsReturned() throws Exception {
        Mockito.when( service.getById( 1L ) )
            .thenReturn( Book.builder().id( 1L ).build() );
        this.mockMvc.perform(
                get( "/books/1" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.id" ).value( 1 ) );
    }

    @Test
    void givenAnExistingTitle_whenRequestByTitle_thenBookIsReturned() throws Exception {
        Mockito.when( service.getByTitle( "Reina Roja" ) )
            .thenReturn( Book.builder().title( "Reina Roja" ).build() );
        this.mockMvc.perform(
                get( "/books/title/Reina Roja" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.title" ).value( "Reina Roja" ) );
    }

    @Test
    void givenAnExistingPartialTitle_whenRequestByTitleLike_thenBooksAreReturned() throws Exception {
        Mockito.when( service.getByTitleLike( "BOHEMIA" ) )
            .thenReturn( List.of(
                Book.builder().title( "Luces de bohemia" ).build()
            ));
        this.mockMvc.perform(
                get( "/books/title-like/BOHEMIA" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.books" ).isArray() )
            .andExpect( jsonPath( "$._embedded.books[?(@.title=='Luces de bohemia')]",
                Matchers.hasSize( 1 ) )
            );
    }

    @Test
    void givenAnExistingIsbn_whenRequestByIsbn_thenBookIsReturned() throws Exception {
        Mockito.when( service.getByIsbn( "9788432221255" ) )
            .thenReturn(
                Book.builder().isbn( "9788432221255" ).build()
            );
        this.mockMvc.perform(
                get( "/books/isbn/9788432221255" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.isbn" ).value( "9788432221255" ) );
    }

    @Test
    void givenAnExistingPagesRange_whenRequestByPages_thenBooksAreReturned() throws Exception {
        Mockito.when( service.getByPages( 0, 100 ) )
            .thenReturn( List.of(
                Book.builder().title( "El arte de la guerra" ).pages( 98 ).build()
            ));
        this.mockMvc.perform(
                get( "/books/pages?min=0&max=100" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.books" ).isArray() )
            .andExpect( jsonPath( "$._embedded.books[?(@.pages > 0 && @.pages <= 100)]",
                Matchers.hasSize( 1 ) )
            );
    }

    @Test
    void givenAnExistingYear_whenRequestByYear_thenBooksAreReturned() throws Exception{
        Mockito.when( service.getByYear( 2016 ) )
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
        this.mockMvc.perform(
                get( "/books/year/2016" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.books" ).isArray() )
            .andExpect( jsonPath( "$._embedded.books[*].year",
                Matchers.everyItem( Matchers.is( 2016 ) )
            ));
    }

    @Test
    void givenAnExistingPublisher_whenRequestByPublisher_thenBooksAreReturned() throws Exception{
        Publisher p = Publisher.builder().name( "Blackie Books" ).build();
        Mockito.when( service.getByPublisher( "Blackie Books" ) )
            .thenReturn( List.of(
                Book.builder().publisher( p ).title( "Calypso" ).build(),
                Book.builder().publisher( p ).title( "Los números de la vida" ).build()
            ));
        this.mockMvc.perform(
                get( "/books/publisher/Blackie Books" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.books" ).isArray() )
            .andExpect( jsonPath( "$._embedded.books[*].publisher.name",
                Matchers.everyItem(
                    Matchers.is( "Blackie Books" ) )
                )
            );
    }

    @Test
    void givenAnExistingAuthor_whenRequestByAuthor_thenBooksAreReturned() throws Exception {
        Author a = Author.builder().name( "Federico" ).surnames( "García Lorca" ).build();
        Mockito.when( service.getByAuthor( "Federico", "García Lorca" ) )
            .thenReturn( List.of(
                Book.builder().author( a ).title( "Bodas de sangre" ).build(),
                Book.builder().author( a ).title( "La casa de Bernarda Alba" ).build(),
                Book.builder().author( a ).title( "Yerma" ).build()
            ));
        this.mockMvc.perform(
                get( "/books/author?name=Federico&surnames=García Lorca" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.books" ).isArray() )
            .andExpect( jsonPath( "$._embedded.books[*].title",
                Matchers.everyItem(
                    Matchers.anyOf(
                        Matchers.is( "Yerma" ),
                        Matchers.is( "Bodas de sangre" ),
                        Matchers.is( "La casa de Bernarda Alba" )
                    )
                )
            ))
            .andExpect( jsonPath(
                "$._embedded.books[*].author[" +
                    "?(@.name == 'Federico' && @.surnames == 'García Lorca')]",
                Matchers.hasSize( 3 )
            ) );
    }

    @Test
    void givenANotExistingPublisher_whenRequestByPublisher_thenNoResultsIsThrown() throws Exception {
        Mockito.when( service.getByPublisher( "Blackie Books" ) )
            .thenThrow( new NoResultsException( "publisher", "Blackie Books" ) );
        this.mockMvc.perform(
                get( "/books/publisher/Blackie Books" ) )
            .andExpect( status().isNotFound() )
            .andExpect( jsonPath("$.message" )
                .value( "no results for publisher with Blackie Books" ) );
    }

    @Test
    void givenANotExistingBook_whenCreate_thenBookIsCreated() throws Exception {
        Book b = Book.builder()
            .id( 1L ).isbn( "9788432221255" ).build();
        BookDto dto = BookDto.builder()
            .id( 1L ).isbn( "9788432221255" ).build();
        Mockito.when( service.create( Mockito.any( Book.class ) ) ).thenReturn( b );
        this.mockMvc.perform(
                post( "/books" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.isbn" ).value( "9788432221255" ) );
    }

    @Test
    void givenAnExistingBook_whenCreate_thenConflictIsReturned() throws Exception {
        BookDto dto = BookDto.builder()
            .id( 1L ).isbn( "9788432221255" ).build();;
        Mockito.when( service.create( Mockito.any( Book.class ) ) )
            .thenThrow( new ExistingBookException( "9788432221255" ) );
        this.mockMvc.perform(
                post( "/books" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isConflict() )
            .andExpect( jsonPath("$.message" )
                .value( "book with isbn 9788432221255 already exists" ) );
    }

    @Test
    void givenAnExistingBook_whenUpdate_thenBookIsUpdated() throws Exception {
        Book b = Book.builder()
            .isbn( "9788432221255" )
            .build();
        BookDto dto = BookDto.builder()
            .isbn( "9788432221255" )
            .build();
        Mockito.when( service.update( Mockito.anyLong(), Mockito.any( Book.class ) ) )
            .thenReturn( b );
        this.mockMvc.perform(
                put( "/books/1" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.isbn" ).value( "9788432221255" ) );
    }

    @Test
    void givenANotExistingBook_whenUpdate_thenNotFoundIsReturned() throws Exception {
        BookDto b = BookDto.builder()
            .id( 1L )
            .build();
        Mockito.when( service.update( Mockito.anyLong(), Mockito.any( Book.class ) ) )
            .thenThrow( new BookNotFoundException( "id", "1" ) );
        this.mockMvc.perform(
                put( "/books/1" )
                    .content( objectMapper.writeValueAsString( b ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isNotFound() )
            .andExpect( jsonPath("$.message" )
                .value( "book not found by id with 1" ) );
    }

    @Test
    void givenABook_whenDelete_thenServiceDeleteIsCalled() throws Exception {
        this.mockMvc.perform( delete( "/books/1" ) );
        Mockito.verify( service,
            Mockito.times( 1 ) ).delete( 1L );
    }

}