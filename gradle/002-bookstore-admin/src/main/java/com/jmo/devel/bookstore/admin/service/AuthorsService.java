package com.jmo.devel.bookstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.admin.dto.AuthorDto;
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

@Service
@ConfigurationProperties(prefix="authors.service")
public class AuthorsService {

    private final RestTemplate        restTemplate;
    private final ObjectMapper        objectMapper;

    @Getter
    private String url;

    public AuthorsService( RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper ) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public AuthorDto create( AuthorDto authorDto ){
        var response = this.restTemplate.postForEntity(
            "", authorDto, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), AuthorDto.class ) :
            AuthorDto.builder().build();
    }

    public List<AuthorDto> getAll(){
        var response = this.restTemplate.getForEntity(
            "", CollectionModel.class
        );
        var body = response.getBody();
        Collection<AuthorDto> authors = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ) :
            List.of();
        return authors.stream().toList();
    }

    public AuthorDto get( long id ){
        var response = this.restTemplate.getForEntity(
            "/" + id, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), AuthorDto.class ) :
            AuthorDto.builder().build();
    }

    public AuthorDto update( long id, AuthorDto authorDto ){
        this.restTemplate.put(
            "/" + id, authorDto
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
