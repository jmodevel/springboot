package com.jmo.devel.tv.shows.dto;

import com.jmo.devel.tv.shows.model.Show;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ShowModelMapper {

    private ModelMapper modelMapper;

    public ShowModelMapper(ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    public ShowDto convertToDto( Show show ) {
        return modelMapper.map( show, ShowDto.class );
    }

}
