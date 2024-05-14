package com.jmo.devel.bookstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
@ConfigurationProperties(prefix="publishers.service")
public class PublishersService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Getter
    private String url;

    public PublishersService( RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper ) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public PublisherDto create( PublisherDto publisherDto ){
        var response = this.restTemplate.postForEntity(
            "", publisherDto, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), PublisherDto.class ) :
            PublisherDto.builder().build();
    }

    public List<PublisherDto> getAll(){
        var response = this.restTemplate.getForEntity(
            "", CollectionModel.class
        );
        var body = response.getBody();
        Collection<PublisherDto> publishers = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ):
            List.of();
        return publishers.stream().toList();
    }

    public PublisherDto get( long id ){
        var response = this.restTemplate.getForEntity(
            "/" + id, EntityModel.class
        );
        var entityModel = response.getBody();
        return ( entityModel != null )?
            objectMapper.convertValue( entityModel.getContent(), PublisherDto.class ) :
            PublisherDto.builder().build();
    }

    public PublisherDto update( long id, PublisherDto publisherDto ){
        this.restTemplate.put(
            "/" + id, publisherDto
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
