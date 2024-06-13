package com.jmo.devel.bookstore.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.api.dto.PublisherDto;
import com.jmo.devel.bookstore.api.exception.ExistingPublisherException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.exception.PublisherNotFoundException;
import com.jmo.devel.bookstore.api.model.Publisher;
import com.jmo.devel.bookstore.api.service.PublishersService;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublishersController.class)
@ExtendWith(SpringExtension.class)
@ComponentScan( basePackages = {
    "com.jmo.devel.bookstore.api.assembler",
    "com.jmo.devel.bookstore.api.configuration",
    "com.jmo.devel.bookstore.api.dto.mapper"
})
class PublishersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublishersService service;

    @Test
    void givenAnExistingPublisher_whenRequestById_thenPublisherIsReturned() throws Exception {
        Mockito.when( service.getById( 1L ) )
            .thenReturn( Publisher.builder().id( 1L ).build() );
        this.mockMvc.perform(
                get( "/publishers/1" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.id" ).value( 1 ) );
    }

    @Test
    void givenAnExistingName_whenRequestByName_thenPublisherIsReturned() throws Exception {
        Mockito.when( service.getByName( "Blackie Books" ) )
            .thenReturn(
                Publisher.builder().name( "Blackie Books" ).build()
            );
        this.mockMvc.perform(
                get( "/publishers/name/Blackie Books" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$.name" ).value( "Blackie Books" ) );
    }

    @Test
    void givenAnExistingCity_whenRequestByHeadquarters_thenPublishersAreReturned() throws Exception {
        Mockito.when( service.getByHeadquarters( List.of( "Barcelona" ) ) )
            .thenReturn( List.of(
                Publisher.builder().name( "Planeta" ).headquarters( "Barcelona" ).build(),
                Publisher.builder().name( "Blackie Books" ).headquarters( "Barcelona" ).build()
            ));
        this.mockMvc.perform(
                get( "/publishers/headquarters?headquarters=Barcelona" ) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath( "$._embedded.publishers" ).isArray() )
            .andExpect( jsonPath( "$._embedded.publishers[*].name",
                Matchers.everyItem(
                    Matchers.anyOf(
                        Matchers.is( "Planeta" ),
                        Matchers.is( "Blackie Books" )
                    )
                )
            ))
            .andExpect( jsonPath(
                "$._embedded.publishers[?(@.headquarters == 'Barcelona')]",
                Matchers.hasSize( 2 )
            ) );
    }

    @Test
    void givenANotExistingCity_whenRequestByHeadquarters_thenNoResultsIsThrown() throws Exception {
        Mockito.when( service.getByHeadquarters( List.of( "Barcelona" ) ))
            .thenThrow( new NoResultsException( "headquarters", "Barcelona" ) );
        this.mockMvc.perform(
                get( "/publishers/headquarters?headquarters=Barcelona" ) )
            .andExpect( status().isNotFound() )
            .andExpect( jsonPath("$.message" )
                .value( "no results for headquarters with Barcelona" ) );
    }

    @Test
    void givenANotExistingPublisher_whenCreate_thenPublisherIsCreated() throws Exception {
        Publisher p = Publisher.builder()
            .id( 1L ).name( "Planeta" ).build();
        PublisherDto dto = PublisherDto.builder()
            .id( 1L ).name( "Planeta" ).build();
        Mockito.when( service.create( Mockito.any( Publisher.class ) ) ).thenReturn( p );
        this.mockMvc.perform(
                post( "/publishers" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name" ).value( "Planeta" ) );
    }

    @Test
    void givenAnExistingPublisher_whenCreate_thenConflictIsReturned() throws Exception {
        PublisherDto dto = PublisherDto.builder()
            .id( 1L ).name( "Planeta" ).build();
        Mockito.when( service.create( Mockito.any( Publisher.class ) ) )
            .thenThrow( new ExistingPublisherException( "Planeta" ) );
        this.mockMvc.perform(
                post( "/publishers" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isConflict() )
            .andExpect( jsonPath("$.message" )
                .value( "publisher with name Planeta already exists" ) );
    }

    @Test
    void givenAnExistingPublisher_whenUpdate_thenPublisherIsUpdated() throws Exception {
        Publisher p = Publisher.builder()
            .id( 1L ).name( "Planeta" ).build();
        PublisherDto dto = PublisherDto.builder()
            .id( 1L ).name( "Planeta" ).build();
        Mockito.when( service.update( Mockito.anyLong(), Mockito.any( Publisher.class ) ) )
            .thenReturn( p );
        this.mockMvc.perform(
                put( "/publishers/1" )
                    .content( objectMapper.writeValueAsString( dto ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.id" ).value( 1 ) )
            .andExpect( jsonPath("$.name" ).value( "Planeta" ) );
    }

    @Test
    void givenANotExistingPublisher_whenUpdate_thenNotFoundIsReturned() throws Exception {
        Publisher p = Publisher.builder()
            .id( 1L ).name( "Planeta" ).build();;
        Mockito.when( service.update( Mockito.anyLong(), Mockito.any( Publisher.class ) ) )
            .thenThrow( new PublisherNotFoundException( "id", "1" ) );
        this.mockMvc.perform(
                put( "/publishers/1" )
                    .content( objectMapper.writeValueAsString( p ) )
                    .contentType( MediaType.APPLICATION_JSON ))
            .andExpect( status().isNotFound() )
            .andExpect( jsonPath("$.message" )
                .value( "publisher not found by id with 1" ) );
    }

    @Test
    void givenAPublisher_whenDelete_thenServiceDeleteIsCalled() throws Exception {
        this.mockMvc.perform( delete( "/publishers/1" ) );
        Mockito.verify( service,
            Mockito.times( 1 ) ).delete( 1L );
    }

}