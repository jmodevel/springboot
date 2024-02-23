package com.jmo.devel.tv.shows.controller;

import com.jmo.devel.tv.shows.model.Show;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class ShowsControllerPortTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void givenAnExistingGenre_whenShowsAreRequested_thenMoreThanOneIsReturned() throws Exception {
        ResponseEntity<Show[]> response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/shows/genre/Science-Fiction", Show[].class);
        Assertions.assertThat( response.getBody() )
                .hasSizeGreaterThan(0);
        Assertions.assertThat( response.getBody() )
                .allMatch(
                    s -> Arrays.asList( s.getGenres() ).contains( "Science-Fiction" ) );
    }

    @ParameterizedTest
    @ValueSource(strings={
            "/shows/genre/None",
            "/shows/type/None",
            "/shows/status/None",
            "/shows/runtime/13"
    })
    void givenANonExistingValue_whenShowsAreRequested_thenNoResultsExceptionIsThrown( String path ) throws Exception {
        ResponseEntity<String> response = this.restTemplate
                .getForEntity("http://localhost:" + port + path, String.class);
        Assertions.assertThat( response.getStatusCode() )
                .isEqualTo( HttpStatus.NOT_FOUND );
        Assertions.assertThat( response.getBody() )
                .startsWith( "no results" );
    }

    @Test
    void givenAnExistingType_whenShowsAreRequested_thenMoreThanOneIsReturned() throws Exception {
        ResponseEntity<Show[]> response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/shows/type/Scripted", Show[].class);
        Assertions.assertThat( response.getBody() )
                .hasSizeGreaterThan(0);
        Assertions.assertThat( response.getBody() )
                .allMatch( s -> s.getType().equals( "Scripted" ) );
    }

    @Test
    void givenAnExistingStatus_whenShowsAreRequested_thenMoreThanOneIsReturned() throws Exception {
        ResponseEntity<Show[]> response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/shows/status/Ended", Show[].class);
        Assertions.assertThat( response.getBody() )
                .hasSizeGreaterThan(0);
        Assertions.assertThat( response.getBody() )
                .allMatch( s -> s.getStatus().equals( "Ended" ) );
    }

    @Test
    void givenAnExistingRuntime_whenShowsAreRequested_thenMoreThanOneIsReturned() throws Exception {
        ResponseEntity<Show[]> response = this.restTemplate
                .getForEntity("http://localhost:" + port + "/shows/runtime/42", Show[].class);
        Assertions.assertThat( response.getBody() )
                .hasSizeGreaterThan(0);
        Assertions.assertThat( response.getBody() )
                .allMatch( s -> s.getRuntime() == 42 );
    }

}