package com.jmo.devel.tv.shows.controller;

import com.jmo.devel.tv.shows.dto.ShowDto;
import com.jmo.devel.tv.shows.dto.ShowModelMapper;
import com.jmo.devel.tv.shows.model.Show;
import com.jmo.devel.tv.shows.service.ShowsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("shows")
public class ShowsController {

    private final ShowsService    service;
    private final ShowModelMapper modelMapper;

    public ShowsController(
            ShowsService    service,
            ShowModelMapper modelMapper
    ){
        this.service     = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ShowDto> findItemByName( @PathVariable String name ){
        Show show = this.service.findItemByName( name );
        return ResponseEntity.ok( modelMapper.convertToDto( show ) );
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<ShowDto>> findByGenre( @PathVariable String genre ){
        List<Show> shows = this.service.findByGenre( genre );
        return ResponseEntity.ok(
                shows.stream().map( modelMapper::convertToDto ).toList()
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ShowDto>> findByType( @PathVariable String type ){
        List<Show> shows = this.service.findByType( type );
        return ResponseEntity.ok(
                shows.stream().map( modelMapper::convertToDto ).toList()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShowDto>> findByStatus( @PathVariable String status ){
        List<Show> shows = this.service.findByStatus( status );
        return ResponseEntity.ok(
                shows.stream().map( modelMapper::convertToDto ).toList()
        );
    }

    @GetMapping("/runtime/{runtime}")
    public ResponseEntity<List<ShowDto>> findByRuntime( @PathVariable int runtime ){
        List<Show> shows = this.service.findByRuntime( runtime );
        return ResponseEntity.ok(
                shows.stream().map( modelMapper::convertToDto ).toList()
        );
    }

}
