package com.jmo.devel.bookstore.api.dto.mapper;

import com.jmo.devel.bookstore.api.dto.BookDto;
import com.jmo.devel.bookstore.api.model.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BooksModelMapper {

    private ModelMapper modelMapper;

    public BooksModelMapper( ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    public BookDto toDto( Book book ){
        return this.modelMapper.map( book, BookDto.class );
    }

    public Book fromDto( BookDto book ){
        return this.modelMapper.map( book, Book.class );
    }

}
