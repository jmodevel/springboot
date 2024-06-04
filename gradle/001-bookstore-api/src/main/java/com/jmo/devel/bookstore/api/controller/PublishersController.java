package com.jmo.devel.bookstore.api.controller;

import com.jmo.devel.bookstore.api.assembler.PublishersModelAssembler;
import com.jmo.devel.bookstore.api.dto.PublisherDto;
import com.jmo.devel.bookstore.api.dto.mapper.PublishersModelMapper;
import com.jmo.devel.bookstore.api.hateoas.City;
import com.jmo.devel.bookstore.api.model.Publisher;
import com.jmo.devel.bookstore.api.service.PublishersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("publishers")
@Slf4j
public class PublishersController {

    @Value("${server.port}")
    private int port;

    private final PublishersService        service;
    private final PublishersModelMapper    modelMapper;
    private final PublishersModelAssembler modelAssembler;

    public PublishersController(
        PublishersService        service,
        PublishersModelMapper    modelMapper,
        PublishersModelAssembler modelAssembler
    ){
        this.service        = service;
        this.modelMapper    = modelMapper;
        this.modelAssembler = modelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<PublisherDto>> create( @RequestBody PublisherDto publisher ){
        return ok( this.service.create( this.modelMapper.fromDto( publisher ) ) );
    }

    @GetMapping( { "/", "" } )
    public ResponseEntity<CollectionModel<EntityModel<PublisherDto>>> getAll(){
        log.info( "Serving all publishers from port: " + port );
        return ok( this.service.getAll() );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<EntityModel<PublisherDto>> getById( @PathVariable( "id" ) Long id ){
        return ok( this.service.getById( id ) );
    }

    @GetMapping( "/name/{name}" )
    public ResponseEntity<EntityModel<PublisherDto>> getByName( @PathVariable( "name" ) String name ){
        return ok( this.service.getByName( name ) );
    }

    @GetMapping( "/headquarters" )
    public ResponseEntity<CollectionModel<EntityModel<PublisherDto>>> getByHeadquarters(
        @RequestParam( "headquarters" ) List<String> headquarters
    ){
        return ok( this.service.getByHeadquarters( headquarters ) );
    }

    @GetMapping( "/cities" )
    public ResponseEntity<CollectionModel<City>> getCities(){
        return success( this.service.getCities() );
    }

    @PutMapping( "/{id}" )
    public ResponseEntity<EntityModel<PublisherDto>> update(
        @PathVariable( "id" ) Long id,
        @RequestBody PublisherDto publisher
    ){
        return ok(
            this.service.update( id, this.modelMapper.fromDto( publisher ) )
        );
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<Void> delete( @PathVariable( "id" ) Long id ){
        this.service.delete( id );
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<EntityModel<PublisherDto>> ok(Publisher publisher ){
        return ResponseEntity.ok(
            this.modelAssembler.toModel(
                this.modelMapper.toDto( publisher )
            )
        );
    }

    private ResponseEntity<CollectionModel<EntityModel<PublisherDto>>> ok(List<Publisher> publishers ){
        return ResponseEntity.ok(
            this.modelAssembler.toCollectionModel(
                publishers.stream().map( modelMapper::toDto ).toList()
            )
        );
    }

    private ResponseEntity<CollectionModel<City>> success( List<String> results ){
        return ResponseEntity.ok(
            CollectionModel.of( results.stream().map( City::new ).toList() )
        );
    }

}
