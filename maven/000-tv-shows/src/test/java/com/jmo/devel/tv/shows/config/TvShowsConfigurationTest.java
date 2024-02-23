package com.jmo.devel.tv.shows.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TvShowsConfigurationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenAppConfiguration_whenConfigurationIsLoaded_thenObjectMapperIsNotNull() {
        Assertions.assertNotNull( objectMapper );
    }

}
