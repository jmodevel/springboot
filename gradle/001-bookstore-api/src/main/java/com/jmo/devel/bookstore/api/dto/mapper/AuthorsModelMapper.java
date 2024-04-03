package com.jmo.devel.bookstore.api.dto.mapper;

import com.jmo.devel.bookstore.api.dto.AuthorDto;
import com.jmo.devel.bookstore.api.model.Author;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorsModelMapper {

    private ModelMapper modelMapper;

    public AuthorsModelMapper( ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    public AuthorDto toDto( Author author ){
        return this.modelMapper.map( author, AuthorDto.class );
    }

    public Author fromDto( AuthorDto author ){
        return this.modelMapper.map( author, Author.class );
    }

}
