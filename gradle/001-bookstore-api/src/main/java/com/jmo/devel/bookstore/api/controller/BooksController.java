package com.jmo.devel.bookstore.api.controller;

import com.jmo.devel.bookstore.api.assembler.BooksModelAssembler;
import com.jmo.devel.bookstore.api.dto.BookDto;
import com.jmo.devel.bookstore.api.dto.mapper.BooksModelMapper;
import com.jmo.devel.bookstore.api.model.Book;
import com.jmo.devel.bookstore.api.service.BooksService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class BooksController {

    private final BooksService        service;
    private final BooksModelMapper    modelMapper;
    private final BooksModelAssembler modelAssembler;

    public BooksController(
        BooksService        service,
        BooksModelMapper    modelMapper,
        BooksModelAssembler modelAssembler
    ){
        this.service        = service;
        this.modelMapper    = modelMapper;
        this.modelAssembler = modelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<BookDto>> create( @RequestBody BookDto book ){
        return ok( this.service.create( this.modelMapper.fromDto( book ) ) );
    }

    @GetMapping( { "/", "" } )
    public ResponseEntity<CollectionModel<EntityModel<BookDto>>> getAll(){
        return ok( this.service.getAll() );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<EntityModel<BookDto>> getById( @PathVariable( "id" ) Long id ){
        return ok( this.service.getById( id ) );
    }

    @GetMapping( "/title/{title}" )
    public ResponseEntity<EntityModel<BookDto>> getByTitle(
        @PathVariable( "title" ) String title
    ){
        return ok( this.service.getByTitle( title ) );
    }

    @GetMapping( "/title-like/{title}" )
    public ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByTitleLike(
        @PathVariable( "title" ) String title
    ){
        return ok( this.service.getByTitleLike( title ) );
    }

    @GetMapping( "/isbn/{isbn}" )
    public ResponseEntity<EntityModel<BookDto>> getByIsbn(
        @PathVariable( "isbn" ) String isbn
    ){
        return ok( this.service.getByIsbn( isbn ) );
    }

    @GetMapping( "/pages" )
    public ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByPages(
        @RequestParam( "min" ) int min,
        @RequestParam( "max" ) int max
    ){
        return ok( this.service.getByPages( min, max ) );
    }

    @GetMapping( "/year/{year}" )
    public ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByYear(
        @PathVariable("year") int year
    ){
        return ok( this.service.getByYear( year ) );
    }

    @GetMapping( "/publisher/{publisher}" )
    public ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByPublisher(
        @PathVariable("publisher") String publisher
    ){
        return ok( this.service.getByPublisher( publisher ) );
    }

    @GetMapping( "/author" )
    public ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByAuthor(
        @RequestParam( "name"     ) String name,
        @RequestParam( "surnames" ) String surnames
    ){
        return ok( this.service.getByAuthor( name, surnames ) );
    }

    @PutMapping( "/{id}" )
    public ResponseEntity<EntityModel<BookDto>> update(
        @PathVariable( "id" ) Long id,
        @RequestBody BookDto book
    ){
        return ok( this.service.update( id, this.modelMapper.fromDto( book ) ) );
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<Void> delete( @PathVariable( "id" ) Long id ){
        this.service.delete( id );
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<EntityModel<BookDto>> ok( Book book ){
        return ResponseEntity.ok(
            this.modelAssembler.toModel(
                this.modelMapper.toDto( book )
            )
        );
    }

    private ResponseEntity<CollectionModel<EntityModel<BookDto>>> ok( List<Book> books ){
        return ResponseEntity.ok(
            this.modelAssembler.toCollectionModel(
                books.stream().map( modelMapper::toDto ).toList()
            )
        );
    }

}
