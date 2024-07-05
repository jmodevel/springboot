package com.jmo.devel.zuul.testing.server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// ImageFromDockerfile creates image on-the-fly. Only Docker running is required for this test
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ZuulServerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Container
    private static final GenericContainer<?> ping =
        new GenericContainer<>(
            new ImageFromDockerfile()
                .withDockerfile( Paths.get( "..", "002-2-ping", "Dockerfile" ) )
        ).withExposedPorts(8081);

    @Container
    private static final GenericContainer<?> pong =
        new GenericContainer<>(
            new ImageFromDockerfile()
                .withDockerfile( Paths.get( "..", "002-3-pong", "Dockerfile" ) )
        ).withExposedPorts(8082);

    private static RestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {

        restTemplate = new RestTemplateBuilder().build();

        ping.start();
        pong.start();

    }

    @AfterAll
    public static void tearDown() {
        ping.stop();
        pong.stop();
    }

    @DynamicPropertySource
    static void dynamicProperties( DynamicPropertyRegistry registry ) {
        registry.add("ping.port", ping::getFirstMappedPort );
        registry.add("pong.port", pong::getFirstMappedPort );
    }

    @Test
    public void testPing() {
        String url = "http://" + ping.getHost() + ":" + ping.getFirstMappedPort();
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).isEqualTo("Ping");
    }

    @Test
    public void testPong() {
        String url = "http://" + pong.getHost() + ":" + pong.getFirstMappedPort();
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).isEqualTo("Pong");
    }

    @Test
    public void testZuulGatewayPing() throws Exception {
        mvc.perform( MockMvcRequestBuilders.get("/api/ping").accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("Ping")));
    }

    @Test
    public void testZuulGatewayPong() throws Exception {
        mvc.perform( MockMvcRequestBuilders.get("/api/pong").accept( MediaType.APPLICATION_JSON ))
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("Pong")));
    }


}
