package com.jmo.devel.bookstore.api.controller;

import com.jmo.devel.bookstore.api.assembler.AuthorsModelAssembler;
import com.jmo.devel.bookstore.api.dto.AuthorDto;
import com.jmo.devel.bookstore.api.dto.mapper.AuthorsModelMapper;
import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.service.AuthorsService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping( value="authors" )
public class AuthorsController {

    private final AuthorsService        service;
    private final AuthorsModelMapper    modelMapper;
    private final AuthorsModelAssembler modelAssembler;

    public AuthorsController(
        AuthorsService        service,
        AuthorsModelMapper    modelMapper,
        AuthorsModelAssembler modelAssembler
    ){
        this.service        = service;
        this.modelMapper    = modelMapper;
        this.modelAssembler = modelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<AuthorDto>> create(
        @RequestBody AuthorDto author
    ) {
        return ok( this.service.create(this.modelMapper.fromDto(author)) );
    }

    @GetMapping( { "" } )
    public ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getAll(){
        return ok( this.service.getAll() );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<EntityModel<AuthorDto>> getById( @PathVariable( "id" ) Long id ){
        return ok( this.service.getById( id ) );
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getByName(
        @PathVariable( "name" ) String name
    ){
        return ok( this.service.getByName( name ) );
    }

    @GetMapping("/surnames/{surnames}")
    public ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getBySurnames(
        @PathVariable( "surnames" ) String surnames
    ){
        return ok( this.service.getBySurnames( surnames ) );
    }

    @GetMapping( "/surnames-like/{surnames}" )
    public ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getBySurnamesLike(
        @PathVariable( "surnames" ) String surnames
    ){
        return ok( this.service.getBySurnamesLike( surnames ) );
    }

    @GetMapping( "/" )
    public ResponseEntity<EntityModel<AuthorDto>> getByNameAndSurnames(
        @RequestParam( value = "name" )     String name,
        @RequestParam( value = "surnames" ) String surnames
    ){
        return ok( this.service.getByNameAndSurnames( name, surnames ) );
    }

    @GetMapping( "/alive")
    public ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getAlive(){
        return ok( this.service.getAlive() );
    }

    @GetMapping( "/born/{year}")
    public ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getByBirthDateAfter(
        @PathVariable int year
    ){
        return ok( this.service.getByBirthDateAfter( LocalDate.of( year, Month.JANUARY, 1 ) ) );
    }

    @PutMapping( "/{id}" )
    public ResponseEntity<EntityModel<AuthorDto>> update(
        @PathVariable("id") Long id,
        @RequestBody AuthorDto author
    ){
        return ok( this.service.update( id, this.modelMapper.fromDto( author ) ) );
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<Void> delete( @PathVariable("id") Long id ){
        this.service.delete( id );
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<EntityModel<AuthorDto>> ok( Author author ){
        return ResponseEntity.ok(
            this.modelAssembler.toModel(
                this.modelMapper.toDto( author )
            )
        );
    }

    private ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> ok( List<Author> authors ){
        return ResponseEntity.ok(
            this.modelAssembler.toCollectionModel(
                authors.stream().map( modelMapper::toDto ).toList()
            )
        );
    }

}
