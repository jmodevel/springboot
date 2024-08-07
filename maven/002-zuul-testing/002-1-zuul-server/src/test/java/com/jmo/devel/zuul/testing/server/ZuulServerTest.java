package com.jmo.devel.zuul.testing.server;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ZuulServerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getPing() throws Exception {
        mvc.perform( MockMvcRequestBuilders.get("/api/ping").accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("Ping")));
    }

    @Test
    public void getPong() throws Exception {
        mvc.perform( MockMvcRequestBuilders.get("/api/pong").accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("Pong")));
    }

}