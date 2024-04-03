package com.jmo.devel.bookstore.api.configuration;

import com.jmo.devel.bookstore.api.dto.AuthorDto;
import com.jmo.devel.bookstore.api.dto.PublisherDto;
import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.model.Publisher;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookstoreApiConfiguration {

    @Bean
    public ModelMapper getModelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap( Publisher.class, PublisherDto.class)
            .addMappings( mapper -> mapper.skip( PublisherDto::setBooks ) );
        modelMapper.createTypeMap( Author.class, AuthorDto.class)
            .addMappings( mapper -> mapper.skip( AuthorDto::setBooks ) );
        return modelMapper;
    }

}
