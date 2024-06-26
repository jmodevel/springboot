package com.jmo.devel.bookstore.api.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.api.dto.AuthorDto;
import com.jmo.devel.bookstore.api.dto.BookDto;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class BooksTest {

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
    void givenBook_whenItIsCreatedReadUpdatedAndDeleted_thenItIsDeleted(){

        String path = "http://localhost:" + port + "/books";

        PublisherDto publisher = getPublisher();
        AuthorDto    author    = getAuthor();

        BookDto book = BookDto.builder()
            .isbn( "9788419654328" )
            .title( "Quijote liberado" )
            .pages( 832 )
            .published( LocalDate.of( 2023, Month.OCTOBER, 18 ) )
            .publisher( publisher )
            .author( author )
            .build();

        var existingResponse = this.restTemplate.getForEntity(
            path + "/isbn/9788419654328", EntityModel.class
        );
        if( existingResponse.getStatusCode().is2xxSuccessful() ){
            BookDto existing = objectMapper.convertValue(
                existingResponse.getBody().getContent(), BookDto.class
            );
            this.restTemplate.delete( path + "/" + existing.getId() );
        }

        var createResponse = this.restTemplate.postForEntity(
            path, book, EntityModel.class
        );

        Assertions.assertThat( createResponse ).isInstanceOf( ResponseEntity.class );
        Assertions.assertThat( createResponse.getStatusCode() ).isEqualTo(
            HttpStatusCode.valueOf( HttpStatus.OK.value() )
        );
        EntityModel<BookDto> entity = (EntityModel<BookDto>) createResponse.getBody();
        Assertions.assertThat( entity ).isNotNull();
        BookDto created = objectMapper.convertValue(
            entity.getContent(), BookDto.class
        );
        Assertions.assertThat( created )
            .hasFieldOrPropertyWithValue( "isbn", "9788419654328" )
            .hasFieldOrPropertyWithValue( "title", "Quijote liberado" )
            .hasFieldOrPropertyWithValue( "pages", 832 )
            .hasFieldOrPropertyWithValue( "published",
                LocalDate.of( 2023, Month.OCTOBER, 18 ) );
        Assertions.assertThat( created.getPublisher() ).isNotNull()
            .hasFieldOrPropertyWithValue( "name", "Blackie Books" );
        Assertions.assertThat( created.getAuthor() ).isNotNull()
            .hasFieldOrPropertyWithValue( "name", "Miguel" )
            .hasFieldOrPropertyWithValue( "surnames", "de Cervantes" );

        var byIdResponse = this.restTemplate.getForEntity(
            path + "/" + created.getId(), EntityModel.class
        );
        Assertions.assertThat( byIdResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        var byTitleResponse = this.restTemplate.getForEntity(
            path + "/title/Quijote Liberado", EntityModel.class
        );
        Assertions.assertThat( byTitleResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        var byTitleLike = this.restTemplate.getForEntity(
            path + "/title-like/Quijote", CollectionModel.class
        );
        Assertions.assertThat( byTitleLike.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<BookDto>> byTitleLikeBooks = objectMapper.convertValue(
            byTitleLike.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( byTitleLikeBooks )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var byIsbnResponse = this.restTemplate.getForEntity(
            path + "/isbn/9788419654328", EntityModel.class
        );
        Assertions.assertThat( byIsbnResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        var byPages = this.restTemplate.getForEntity(
            path + "/pages?min=500&max=1000", CollectionModel.class
        );
        Assertions.assertThat( byPages.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<BookDto>> byPagesBooks = objectMapper.convertValue(
            byPages.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( byPagesBooks )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var byYear = this.restTemplate.getForEntity(
            path + "/year/2023", CollectionModel.class
        );
        Assertions.assertThat( byYear.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<BookDto>> byYearBooks = objectMapper.convertValue(
            byYear.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( byYearBooks )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var byPublisher = this.restTemplate.getForEntity(
            path + "/publisher/Blackie Books", CollectionModel.class
        );
        Assertions.assertThat( byPublisher.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<BookDto>> byPublisherBooks = objectMapper.convertValue(
            byPublisher.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( byPublisherBooks )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );

        var byAuthor = this.restTemplate.getForEntity(
            path + "/author?name=Miguel&surnames=de Cervantes", CollectionModel.class
        );
        Assertions.assertThat( byAuthor.getStatusCode().is2xxSuccessful() ).isTrue();
        Collection<EntityModel<BookDto>> byAuthorBooks = objectMapper.convertValue(
            byAuthor.getBody().getContent(), new TypeReference<>(){}
        );
        Assertions.assertThat( byAuthorBooks )
            .anyMatch( p -> p.getContent().getId().equals( created.getId() ) );


        created.setTitle( "Quijote" );
        this.restTemplate.put( path + "/" + created.getId(), created );

        var byUpdatedTitleResponse = this.restTemplate.getForEntity(
            path + "/title/Quijote", EntityModel.class
        );

        Assertions.assertThat( byUpdatedTitleResponse.getStatusCode().is2xxSuccessful() ).isTrue();

        this.restTemplate.delete( path + "/" + created.getId() );

        // cache applies for this method
        /*var afterDeleteResponse = this.restTemplate.getForEntity(
            path + "/" + created.getId(), EntityModel.class
        );*/
        var afterDeleteResponse = this.restTemplate.getForEntity(
            path + "/title/Quijote", EntityModel.class
        );
        Assertions.assertThat( afterDeleteResponse.getStatusCode() ).isEqualTo(
            HttpStatusCode.valueOf( HttpStatus.NOT_FOUND.value() )
        );

    }

    private PublisherDto getPublisher(){
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
            return objectMapper.convertValue(
                existingResponse.getBody().getContent(), PublisherDto.class
            );
        }
        var createResponse = this.restTemplate.postForEntity(
            path, publisher, EntityModel.class
        );
        EntityModel<PublisherDto> entity = (EntityModel<PublisherDto>) createResponse.getBody();
        return objectMapper.convertValue(
            entity.getContent(), PublisherDto.class
        );
    }

    private AuthorDto getAuthor(){
        String path = "http://localhost:" + port + "/authors";
        AuthorDto author = AuthorDto.builder()
            .name( "Miguel" )
            .surnames( "de Cervantes" )
            .birthDate( LocalDate.of( 1547, Month.SEPTEMBER, 29 ) )
            .death( LocalDate.of( 1616, Month.APRIL, 22 ) )
            .build();
        var existingResponse = this.restTemplate.getForEntity(
            path + "/?name=Miguel&surnames=de Cervantes", EntityModel.class
        );
        if( existingResponse.getStatusCode().is2xxSuccessful() ){
            return objectMapper.convertValue(
                existingResponse.getBody().getContent(), AuthorDto.class
            );
        }

        var createResponse = this.restTemplate.postForEntity(
            path, author, EntityModel.class
        );
        EntityModel<AuthorDto> entity = (EntityModel<AuthorDto>) createResponse.getBody();
        return objectMapper.convertValue(
            entity.getContent(), AuthorDto.class
        );
    }

}
