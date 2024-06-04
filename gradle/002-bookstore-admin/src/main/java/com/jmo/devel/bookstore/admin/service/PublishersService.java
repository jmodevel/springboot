package com.jmo.devel.bookstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import com.jmo.devel.bookstore.admin.hateoas.City;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

@Service
public class PublishersService {

    public static final String PUBLISHERS_URL = "/publishers";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PublishersService( RestTemplate restTemplate, ObjectMapper objectMapper ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public PublisherDto create( PublisherDto publisherDto ){
        var response = this.restTemplate.postForEntity(
            PUBLISHERS_URL, publisherDto, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), PublisherDto.class ) :
            PublisherDto.builder().build();
    }

    public List<PublisherDto> getAll(){
        var response = this.restTemplate.getForEntity(
            PUBLISHERS_URL, CollectionModel.class
        );
        var body = response.getBody();
        Collection<PublisherDto> publishers = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ):
            List.of();
        return publishers.stream().toList();
    }

    public List<String> getCities(){
        var response = this.restTemplate.getForEntity(
            PUBLISHERS_URL + "/cities", CollectionModel.class
        );
        var body = response.getBody();
        Collection<City> publishers = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ):
            List.of();
        return publishers.stream().map( City::getContent ).toList();
    }

    public PublisherDto get( long id ){
        var response = this.restTemplate.getForEntity(
            PUBLISHERS_URL + "/" + id, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), PublisherDto.class ) :
            PublisherDto.builder().build();
    }

    public PublisherDto update( long id, PublisherDto publisherDto ){
        this.restTemplate.put(
            PUBLISHERS_URL + "/" + id, publisherDto
        );
        return this.get( id );
    }

    public void delete( long id ){
        this.restTemplate.delete(
            PUBLISHERS_URL + "/" + id
        );
    }

}
