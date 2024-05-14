package com.jmo.devel.bookstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import com.jmo.devel.bookstore.admin.dto.BookDto;
import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@ConfigurationProperties(prefix="books.service")
public class BooksService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Getter
    private String url;

    public BooksService( RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper ) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public BookDto create( BookDto bookDto ){
        var response = this.restTemplate.postForEntity(
            "", bookDto, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), BookDto.class ):
            BookDto.builder().build();
    }

    public List<BookDto> getAll(){
        return this.get( "", Map.of() );
    }

    public BookDto get( long id ){
        var response = this.restTemplate.getForEntity(
            "/" + id, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), BookDto.class ) :
            BookDto.builder().build();
    }

    public List<BookDto> getByAuthor( AuthorDto a ){
        return this.get(
            "/author?name={name}&surnames={surnames}",
            Map.of( "name", a.getName(), "surnames", a.getSurnames() )
        );
    }

    public List<BookDto> getByPublisher( PublisherDto p ){
        return this.get( "/publisher/" + p.getName(), Map.of() );
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
            "/" + id, bookDto
        );
        return this.get( id );
    }

    public void delete( long id ){
        this.restTemplate.delete(
            "/" + id
        );
    }

    public void setUrl( String url ) {
        this.url = url;
        this.restTemplate.setUriTemplateHandler( new DefaultUriBuilderFactory( this.url ) );
    }

}
