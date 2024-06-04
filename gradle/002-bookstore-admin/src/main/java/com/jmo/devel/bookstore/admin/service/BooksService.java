package com.jmo.devel.bookstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import com.jmo.devel.bookstore.admin.dto.BookDto;
import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class BooksService {

    public static final String BOOKS_URL = "/books";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BooksService( RestTemplate restTemplate, ObjectMapper objectMapper ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public BookDto create( BookDto bookDto ){
        var response = this.restTemplate.postForEntity(
            BOOKS_URL, bookDto, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), BookDto.class ):
            BookDto.builder().build();
    }

    public List<BookDto> getAll(){
        return this.get(BOOKS_URL, Map.of() );
    }

    public BookDto get( long id ){
        var response = this.restTemplate.getForEntity(
            BOOKS_URL + "/" + id, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), BookDto.class ) :
            BookDto.builder().build();
    }

    public List<BookDto> getByAuthor( AuthorDto a ){
        return this.get(
            BOOKS_URL + "/author?name={name}&surnames={surnames}",
            Map.of( "name", a.getName(), "surnames", a.getSurnames() )
        );
    }

    public List<BookDto> getByPublisher( PublisherDto p ){
        return this.get( BOOKS_URL + "/publisher/" + p.getName(), Map.of() );
    }

    private List<BookDto> get( String uri, Map<String, String> vars ){
        var response = this.restTemplate.getForEntity(
            uri, CollectionModel.class, vars
        );
        var body = response.getBody();
        Collection<BookDto> books = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ) :
            List.of();
        return books.stream().toList();
    }

    public BookDto update( long id, BookDto bookDto ){
        this.restTemplate.put(
            BOOKS_URL + "/" + id, bookDto
        );
        return this.get( id );
    }

    public void delete( long id ){
        this.restTemplate.delete(
            BOOKS_URL + "/" + id
        );
    }

}
