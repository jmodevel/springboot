package com.jmo.devel.tv.shows.controller;

import com.jmo.devel.tv.shows.TvShowsApplication;
import com.jmo.devel.tv.shows.service.ShowsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TvShowsApplication.class )
@AutoConfigureMockMvc
class ShowsControllerMockServiceTest {

    @MockBean
    private ShowsService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void givenController_whenRuntimeExceptionIsThrown_thenExceptionIsCaught() throws Exception {
        Mockito.when( service.findByType( "Scripted" ) )
                .thenThrow( new IllegalArgumentException( "unexpected value" ) );
        mvc.perform(
                get( "/shows/type/Scripted" )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isInternalServerError() )
                .andExpect( content().string( "unexpected value" ) );
    }


}
