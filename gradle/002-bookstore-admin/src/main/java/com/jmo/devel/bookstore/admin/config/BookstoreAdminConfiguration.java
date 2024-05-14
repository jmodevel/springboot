package com.jmo.devel.bookstore.admin.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.HypermediaRestTemplateConfigurer;

@Configuration
public class BookstoreAdminConfiguration {

    @Bean
    RestTemplateCustomizer hypermediaRestTemplateCustomizer(
        HypermediaRestTemplateConfigurer configurer
    ) {
        return configurer::registerHypermediaTypes;
    }



}
