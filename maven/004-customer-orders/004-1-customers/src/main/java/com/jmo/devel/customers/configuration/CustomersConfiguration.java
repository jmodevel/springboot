package com.jmo.devel.customers.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomersConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }


}
