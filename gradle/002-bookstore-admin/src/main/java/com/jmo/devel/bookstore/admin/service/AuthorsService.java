package com.jmo.devel.bookstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

@Service
public class AuthorsService {

    public static final String AUTHORS_URL = "/authors";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AuthorsService( RestTemplate restTemplate, ObjectMapper objectMapper ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public AuthorDto create( AuthorDto authorDto ){
        var response = this.restTemplate.postForEntity(
            AUTHORS_URL, authorDto, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), AuthorDto.class ) :
            AuthorDto.builder().build();
    }

    public List<AuthorDto> getAll(){
        var response = this.restTemplate.getForEntity(
            AUTHORS_URL, CollectionModel.class
        );
        var body = response.getBody();
        Collection<AuthorDto> authors = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ) :
            List.of();
        return authors.stream().toList();
    }

    public AuthorDto get( long id ){
        var response = this.restTemplate.getForEntity(
            AUTHORS_URL + "/" + id, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), AuthorDto.class ) :
            AuthorDto.builder().build();
    }

    public AuthorDto update( long id, AuthorDto authorDto ){
        this.restTemplate.put(
            AUTHORS_URL + "/" + id, authorDto
        );
        return this.get( id );
    }

    public void delete( long id ){
        this.restTemplate.delete(
            AUTHORS_URL + "/" + id
        );
    }

}
