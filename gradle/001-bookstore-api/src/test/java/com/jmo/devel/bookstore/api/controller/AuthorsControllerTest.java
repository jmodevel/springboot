package com.jmo.devel.bookstore.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.api.dto.AuthorDto;
import com.jmo.devel.bookstore.api.exception.AuthorNotFoundException;
import com.jmo.devel.bookstore.api.exception.ExistingAuthorException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.service.AuthorsService;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorsController.class)
@ExtendWith(SpringExtension.class)
@ComponentScan( basePackages = {
    "com.jmo.devel.bookstore.api.assembler",
    "com.jmo.devel.bookstore.api.configuration",
    "com.jmo.devel.bookstore.api.dto.mapper"
})
class AuthorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorsService service;

    @Test
    void givenAnExistingAuthor_whenGetById_thenAuthorIsReturned() throws Exception {
        Mockito.when( service.getById( 1L ) )
            .thenReturn( Author.builder().id(1L).build() );
        this.mockMvc.perform(
                get( "/authors/1" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.id" ).value( 1 ) );
    }

    @Test
    void givenAnExistingAuthor_whenGetByNameAndSurnames_thenAuthorIsReturned() throws Exception {
        Mockito.when( service.getByNameAndSurnames(
                "Ramón", "Valle"
            ))
            .thenReturn(
                Author.builder()
                    .name( "Ramón María" )
                    .surnames( "Del Valle Inclán" )
                    .build()
            );
        this.mockMvc.perform(
                get( "/authors/?name=Ramón&surnames=Valle" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.name").value( "Ramón María" ) );
    }

    @Test
    void givenAName_whenGetByName_thenAuthorsAreReturned() throws Exception {
        Mockito.when( service.getByName( "Jorge Luis" ) )
            .thenReturn( List.of(
                Author.builder().name( "Jorge Luis" ).build()
            ));
    this.mockMvc.perform(
            get( "/authors/name/Jorge Luis" ) )
        .andExpect( status().isOk() )
        .andExpect( jsonPath( "$._embedded.authors").isArray() )
        .andExpect( jsonPath( "$._embedded.authors[0].name").value( "Jorge Luis" ) );
    }

    @Test
    void givenASurnames_whenGetBySurnames_thenAuthorsAreReturned() throws Exception {
        Mockito.when( service.getBySurnames( "Highsmith" ) )
            .thenReturn( List.of(
                Author.builder().surnames( "Highsmith" ).build()
            ));
        this.mockMvc.perform(
                get( "/authors/surnames/Highsmith" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.authors").isArray() )
            .andExpect( jsonPath( "$._embedded.authors[0].surnames").value( "Highsmith" ) );
    }

    @Test
    void givenAnUppercaseSurnames_whenGetBySurnamesLike_thenAuthorsAreReturned() throws Exception {
        Mockito.when( service.getBySurnamesLike( "CHRISTIE" ) )
            .thenReturn( List.of(
                Author.builder().surnames( "Christie" ).build()
            ));
        this.mockMvc.perform(
                get( "/authors/surnames-like/CHRISTIE" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.authors").isArray() )
            .andExpect( jsonPath( "$._embedded.authors[?(@.surnames == 'Christie')]",
                Matchers.hasSize( 1 ) )
            );
    }

    @Test
    void givenAnAuthor_whenGetAlive_thenAuthorsAreReturned() throws Exception {
        Mockito.when( service.getAlive() )
            .thenReturn( List.of(
                Author.builder()
                    .name( "Juan" )
                    .surnames( "Gómez-Jurado" )
                    .build()
            ));
        this.mockMvc.perform(
                get( "/authors/alive" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.authors").isArray() )
            .andExpect( jsonPath( "$._embedded.authors[?(@.death == null)]",
                Matchers.hasSize( Matchers.greaterThanOrEqualTo( 1 ) ) )
            );
    }

    @Test
    void givenAnAuthor_whenGetBornAfter_thenAuthorsAreReturned() throws Exception {
        Mockito.when( service.getByBirthDateAfter(
                LocalDate.of( 1970, Month.JANUARY, 1 )
            ))
            .thenReturn( List.of(
                Author.builder()
                    .name( "Juan" )
                    .surnames( "Gómez-Jurado" )
                    .birthDate( LocalDate.of( 1977, Month.DECEMBER, 16 ) )
                    .build()
            ));
        this.mockMvc.perform(
                get( "/authors/born/1970" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.authors").isArray() )
            .andExpect( jsonPath( "$._embedded.authors.*.birthDate",
                Matchers.everyItem( dateStringGreaterThan( 1970 ) ) )
            );
    }

    @Test
    void givenNoAuthors_whenGetBornAfter_thenNoResultsIsThrown() throws Exception {
        Mockito.when( service.getByBirthDateAfter(
                LocalDate.of( 1970, Month.JANUARY, 1 )
            ) )
            .thenThrow( new NoResultsException( "date", "1970-01-01" ) );
        this.mockMvc.perform(
                get( "/authors/born/1970" ) )
            .andExpect( status().isNotFound() );
    }

    @Test
    void givenANotExistingAuthor_whenCreate_thenAuthorIsCreated() throws Exception {
        Author a = Author.builder()
            .id( 1L ).name( "Jorge Luis" ).surnames( "Borges" )
            .build();
        AuthorDto dto = AuthorDto.builder()
            .name( "Jorge Luis" ).surnames( "Borges" )
            .build();
        Mockito.when( service.create( Mockito.any( Author.class ) ) ).thenReturn( a );
        this.mockMvc.perform(
                post( "/authors" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name" ).value( "Jorge Luis" ) )
            .andExpect( jsonPath("$.surnames" ).value( "Borges" ) );
    }

    @Test
    void givenAnExistingAuthor_whenCreate_thenConflictIsReturned() throws Exception {
        AuthorDto dto = AuthorDto.builder()
            .name( "Jorge Luis" ).surnames( "Borges" )
            .build();
        Mockito.when( service.create( Mockito.any( Author.class ) ) )
            .thenThrow( new ExistingAuthorException( "Jorge Luis", "Borges" ) );
        this.mockMvc.perform(
                post( "/authors" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isConflict() )
            .andExpect( jsonPath("$.message" )
                .value( "author Jorge Luis Borges already exists" ) );
    }

    @Test
    void givenAnExistingAuthor_whenUpdate_thenAuthorIsUpdated() throws Exception {
        Author a = Author.builder()
            .id( 1L ).name( "Patricia" ).surnames( "Highsmith" )
            .build();
        AuthorDto dto = AuthorDto.builder()
            .name( "Patricia" ).surnames( "Highsmith" )
            .build();
        Mockito.when( service.update( Mockito.anyLong(), Mockito.any( Author.class ) ) ).thenReturn( a );
        this.mockMvc.perform(
                put( "/authors/1" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name" ).value( "Patricia" ) )
            .andExpect( jsonPath("$.surnames" ).value( "Highsmith" ) );
    }

    @Test
    void givenANotExistingAuthor_whenUpdate_thenNotFoundIsReturned() throws Exception {
        AuthorDto dto = AuthorDto.builder()
            .name( "Patricia" ).surnames( "Highsmith" )
            .build();
        Mockito.when( service.update( Mockito.anyLong(), Mockito.any( Author.class ) ) )
            .thenThrow( new AuthorNotFoundException( "id", "1" ) );
        this.mockMvc.perform(
                put( "/authors/1" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isNotFound() )
            .andExpect( jsonPath("$.message" )
                .value( "author not found by id with 1" ) );
    }

    @Test
    void givenAnAuthor_whenDelete_thenServiceDeleteIsCalled() throws Exception {
        this.mockMvc.perform( delete( "/authors/1" ) );
        Mockito.verify( service,
            Mockito.times( 1 ) ).delete( 1L );
    }

    public static Matcher<String> dateStringGreaterThan( int year ) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely( String dateString ) {
                LocalDate date = LocalDate.parse( dateString, DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) );
                return date.getYear() > year;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("greater than ").appendValue( year );
            }
        };
    }

}