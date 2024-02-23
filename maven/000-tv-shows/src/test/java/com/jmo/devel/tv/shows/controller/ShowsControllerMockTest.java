package com.jmo.devel.tv.shows.controller;

import com.jmo.devel.tv.shows.TvShowsApplication;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TvShowsApplication.class )
@AutoConfigureMockMvc
class ShowsControllerMockTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void givenShows_whenAnExistingShowIsRequestedByName_thenShowIsReturned() throws Exception {
        mvc.perform(
                get( "/shows/name/24" )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.name", Matchers.is( "24" ) ) );
    }

    @Test
    void givenShows_whenAShowWithSpacesIsRequestedByName_thenShowIsReturned() throws Exception {
        mvc.perform(
                        get( "/shows/name/Under the Dome" )
                                .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.name", Matchers.is( "Under the Dome" ) ) );
    }

    @Test
    void givenShows_whenANonExistingShowIsRequestedByName_thenAShowNotFoundExceptionIsThrown() throws Exception {
        mvc.perform( get( "/shows/name/Barry" )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andExpect( content().string( Matchers.startsWith( "show not found" ) ) );
    }

    @Test
    void givenNoResourcePath_whenPathIsRequested_thenAnInternalServerErrorIsReturned() throws Exception {
        mvc.perform( get( "/" )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isInternalServerError() )
                .andExpect( content().string( Matchers.startsWith( "No static resource" ) ) );
    }

}
