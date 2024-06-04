package com.jmo.devel.bookstore.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BookstoreAdminConfiguration {

    @Value("${bookstore.api.service.url}")
    private String baseUrl;

    @Bean
    RestTemplateCustomizer hypermediaRestTemplateCustomizer(
        HypermediaRestTemplateConfigurer configurer
    ) {
        return configurer::registerHypermediaTypes;
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate( RestTemplateBuilder builder ){
        return builder.rootUri( this.baseUrl ).build();
    }



}
