package com.jmo.devel.bookstore.api.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.api.dto.AuthorDto;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class AuthorsTest {

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
    void givenAuthor_whenItIsCreatedReadUpdatedAndDeleted_thenItIsDeleted(){

        String path = "http://localhost:" + port + "/authors";

        AuthorDto author = AuthorDto.builder()
            .name( "Isabel" )
            .surnames( "Allende" )
            .birthDate( LocalDate.of( 1942, Month.AUGUST, 2 ) )
            .build();

        var existingResponse = this.restTemplate.getForEntity(
            path + "/?name=Isabel&surnames=Allende", EntityModel.class
        );
        if( existingResponse.getStatusCode().is2xxSuccessful() ){
            AuthorDto existing = objectMapper.convertValue(
                existingResponse.getBody().getContent(), AuthorDto.class
            );
            this.restTemplate.delete( path + "/" + existing.getId() );
        }

        var createResponse = this.restTemplate.postForEntity(
            path, author, EntityModel.class
        );

        Assertions.assertThat( createResponse ).isInstanceOf( ResponseEntity.class );
        Assertions.assertThat( createResponse.getStatusCode() ).isEqualTo(
            HttpStatusCode.valueOf( HttpStatus.OK.value() )
        );
        EntityModel<AuthorDto> entity = (EntityModel<AuthorDto>) createResponse.getBody();
        Assertions.assertThat( entity ).isNotNull();
        AuthorDto created = objectMapper.convertValue(
            entity.getContent(), AuthorDto.class
        );
        Assertions.assertThat( created )
            .hasFieldOrPropertyWithValue( "name", "Isabel" )
            .hasFieldOrPropertyWithValue( "surnames", "Allende" )
            .hasFieldOrPropertyWithValue( "birthDate",
                LocalDate.of( 1942, Month.AUGUST, 2 ) );
        Assertions.assertThat( created.getId() ).isNotNull().isPositive();
        Assertions.assertThat( created.getDeath() ).isNull();

        var byIdResponse = this.restTemplate.getForEntity(
            path + "/" + created.getId(), EntityModel.class
        );
        Assertions.assertThat( byIdResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        var byName = this.restTemplate.getForEntity(
            path + "/name/Isabel", CollectionModel.class
        );
        Assertions.assertThat( byName.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<AuthorDto>> byNameAuthors = objectMapper.convertValue(
            byName.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( byNameAuthors )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var bySurnames = this.restTemplate.getForEntity(
            path + "/surnames/Allende", CollectionModel.class
        );
        Assertions.assertThat( bySurnames.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<AuthorDto>> bySurnamesAuthors = objectMapper.convertValue(
            bySurnames.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( bySurnamesAuthors )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var bySurnamesLike = this.restTemplate.getForEntity(
            path + "/surnames-like/All", CollectionModel.class
        );
        Assertions.assertThat( bySurnamesLike.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<AuthorDto>> bySurnamesLikeAuthors = objectMapper.convertValue(
            bySurnamesLike.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( bySurnamesLikeAuthors )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var alive = this.restTemplate.getForEntity(
            path + "/alive", CollectionModel.class
        );
        Assertions.assertThat( alive.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<AuthorDto>> aliveAuthors = objectMapper.convertValue(
            alive.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( aliveAuthors )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var bornAfter1940 = this.restTemplate.getForEntity(
            path + "/born/1940", CollectionModel.class
        );
        Assertions.assertThat( bornAfter1940.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<AuthorDto>> bornAfter1940Authors = objectMapper.convertValue(
            bornAfter1940.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( bornAfter1940Authors )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        created.setSurnames( "Allende Llona" );
        this.restTemplate.put( path + "/" + created.getId(), created );

        var byUpdatedNameResponse = this.restTemplate.getForEntity(
            path + "/?name=Isabel&surnames=Allende Llona", EntityModel.class
        );

        Assertions.assertThat(byUpdatedNameResponse.getStatusCode().is2xxSuccessful()).isTrue();

        this.restTemplate.delete( path + "/" + created.getId() );

        // cache applies for this method
        /*var afterDeleteResponse = this.restTemplate.getForEntity(
            path + "/" + created.getId(), EntityModel.class
        );*/
        var afterDeleteResponse = this.restTemplate.getForEntity(
            path + "/?name=Isabel&surnames=Allende Llona", EntityModel.class
        );
        Assertions.assertThat( afterDeleteResponse.getStatusCode() ).isEqualTo(
            HttpStatusCode.valueOf( HttpStatus.NOT_FOUND.value() )
        );


    }

}
