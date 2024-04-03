package com.jmo.devel.bookstore.api.dto.mapper;

import com.jmo.devel.bookstore.api.dto.PublisherDto;
import com.jmo.devel.bookstore.api.model.Publisher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PublishersModelMapper {

    private ModelMapper modelMapper;

    public PublishersModelMapper(ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    public PublisherDto toDto( Publisher publisher ){
        return this.modelMapper.map( publisher, PublisherDto.class );
    }

    public Publisher fromDto( PublisherDto publisher ){
        return this.modelMapper.map( publisher, Publisher.class );
    }

}
