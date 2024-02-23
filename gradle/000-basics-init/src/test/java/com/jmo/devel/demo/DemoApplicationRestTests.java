package com.jmo.devel.demo;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.*;

import com.jmo.devel.demo.model.Book;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class DemoApplicationRestTests {

    private static final String API_ROOT = "http://localhost:8081/api/books";

    @Test
    void whenGetAllBooks_thenOK() {
        final Response response = RestAssured.get(API_ROOT);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void whenGetBooksByTitle_thenOK() {
        final Book book = createRandomBook();
        createBookAsUri(book);

        final Response response = RestAssured.get(API_ROOT + "/title/" + book.getTitle());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertFalse(response.as(List.class).isEmpty());
    }

    @Test
    void whenGetCreatedBookById_thenOK() {
        final Book book = createRandomBook();
        final String location = createBookAsUri(book);

        final Response response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(book.getTitle(), response.jsonPath()
                .get("title"));
    }

    @Test
    void whenGetNotExistBookById_thenNotFound() {
        final Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    // POST
    @Test
    void whenCreateNewBook_thenCreated() {
        final Book book = createRandomBook();

        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    void whenInvalidBook_thenError() {
        final Book book = createRandomBook();
        book.setAuthor(null);

        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void whenUpdateCreatedBook_thenUpdated() {
        final Book book = createRandomBook();
        final String location = createBookAsUri(book);

        book.setId(Long.parseLong(location.split("api/books/")[1]));
        book.setAuthor("newAuthor");
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .put(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("newAuthor", response.jsonPath()
                .get("author"));

    }

    @Test
    void whenUpdateNotExistingBook_thenBookIdMismatchExceptionIsThrown() {
        final Book book = createRandomBook();
        final String location = createBookAsUri(book);

        book.setId(21);
        book.setAuthor("newAuthor");
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .put(location);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }


    @Test
    void whenDeleteCreatedBook_thenOk() {
        final Book book = createRandomBook();
        final String location = createBookAsUri(book);

        Response response = RestAssured.delete(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    void whenCreatedConcreteBook_thenAttributesAreOK() {
        final Book book = createLordOfTheRingsBook();
        assertNotEquals( 0, book.hashCode() );
        assertTrue( book.toString().endsWith( ", title=The Lord of The Rings, author=J.R.Tolkien]" ) );
    }

    @Test
    void whenConcreteBookComparesToEmptyBook_thenComparisonIsOK() {
        final Book book  = createLordOfTheRingsBook();
        final Book empty = createEmptyBook();
        assertNotEquals( 0, empty.hashCode() );
        assertNotEquals( book, empty );
    }

    // ===============================

    private Book createRandomBook() {
        final Book book = new Book();
        book.setTitle(randomAlphabetic(10));
        book.setAuthor(randomAlphabetic(15));
        return book;
    }

    private Book createEmptyBook() {
        final Book book = new Book();
        book.setTitle(randomAlphabetic(10));
        book.setAuthor(randomAlphabetic(15));
        return book;
    }

    private Book createLordOfTheRingsBook() {
        return new Book( "The Lord of The Rings", "J.R.Tolkien" );
    }

    private String createBookAsUri(Book book) {
        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath()
                .get("id");
    }

}