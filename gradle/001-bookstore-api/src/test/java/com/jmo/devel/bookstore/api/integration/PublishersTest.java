package com.jmo.devel.bookstore.api.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.api.dto.PublisherDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import redis.embedded.RedisServer;

import java.util.Collection;

@SuppressWarnings("unchecked")
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class PublishersTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private static RedisServer server;

    @BeforeAll
    public static void setUp() {
        server = new RedisServer(6379);
        server.start();
    }

    @AfterAll
    public static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void givenPublisher_whenItIsCreatedReadUpdatedAndDeleted_thenItIsDeleted(){

        String path = "http://localhost:" + port + "/publishers";

        PublisherDto publisher = PublisherDto.builder()
            .name( "Blackie Books" )
            .headquarters( "Barcelona" )
            .website( "https://blackiebooks.org/" )
            .build();

        var existingResponse = this.restTemplate.getForEntity(
            path + "/name/Blackie Books", EntityModel.class
        );
        if( existingResponse.getStatusCode().is2xxSuccessful() ){
            PublisherDto existing = objectMapper.convertValue(
                existingResponse.getBody().getContent(), PublisherDto.class
            );
            this.restTemplate.delete( path + "/" + existing.getId() );
        }

        var createResponse = this.restTemplate.postForEntity(
            path, publisher, EntityModel.class
        );

        Assertions.assertThat( createResponse ).isInstanceOf( ResponseEntity.class );
        Assertions.assertThat( createResponse.getStatusCode() ).isEqualTo(
            HttpStatusCode.valueOf( HttpStatus.OK.value() )
        );
        EntityModel<PublisherDto> entity = (EntityModel<PublisherDto>) createResponse.getBody();
        Assertions.assertThat( entity ).isNotNull();
        PublisherDto created = objectMapper.convertValue(
            entity.getContent(), PublisherDto.class
        );
        Assertions.assertThat( created )
            .hasFieldOrPropertyWithValue( "name", "Blackie Books" )
            .hasFieldOrPropertyWithValue( "headquarters", "Barcelona" )
            .hasFieldOrPropertyWithValue( "website", "https://blackiebooks.org/" );
        Assertions.assertThat( created.getId() ).isNotNull()
            .isPositive();

        var byIdResponse = this.restTemplate.getForEntity(
            path + "/" + created.getId(), EntityModel.class
        );
        Assertions.assertThat( byIdResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        var byNameResponse = this.restTemplate.getForEntity(
            path + "/name/Blackie Books", EntityModel.class
        );
        Assertions.assertThat( byNameResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        var byHeadquartersResponse = this.restTemplate.getForEntity(
            path + "/headquarters?headquarters=Madrid,Barcelona", CollectionModel.class
        );
        Assertions.assertThat( byHeadquartersResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        Collection<PublisherDto> publishers = objectMapper.convertValue(
            byHeadquartersResponse.getBody().getContent(),
            new TypeReference<>(){}
        );
        Assertions.assertThat( publishers )
            .hasSizeGreaterThan( 1 )
            .anyMatch( p -> p.getName().equals( "Blackie Books" ) );

        created.setName( "BlacKieBooKs" );
        this.restTemplate.put( path + "/" + created.getId(), created );

        var byUpdatedNameResponse = this.restTemplate.getForEntity(
            path + "/name/BlacKieBooKs", EntityModel.class
        );

        Assertions.assertThat( byUpdatedNameResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        this.restTemplate.delete( path + "/" + created.getId() );

        // cache applies for this method
        /*var afterDeleteResponse = this.restTemplate.getForEntity(
            path + "/" + created.getId(), EntityModel.class
        );*/
        var afterDeleteResponse = this.restTemplate.getForEntity(
            path + "/name/BlacKieBooKs", EntityModel.class
        );
        Assertions.assertThat( afterDeleteResponse.getStatusCode() ).isEqualTo(
            HttpStatusCode.valueOf( HttpStatus.NOT_FOUND.value() )
        );

    }

}
