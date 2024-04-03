package com.jmo.devel.bookstore.api.assembler;

import com.jmo.devel.bookstore.api.controller.BooksController;
import com.jmo.devel.bookstore.api.dto.BookDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BooksModelAssembler implements RepresentationModelAssembler<BookDto, EntityModel<BookDto>> {

    @Override
    public EntityModel<BookDto> toModel( BookDto book ) {
        EntityModel<BookDto> model = EntityModel.of(
            book,
            linkTo( methodOn( BooksController.class ).getById( book.getId() ) ).withSelfRel(),
            linkTo( methodOn( BooksController.class ).getByIsbn( book.getIsbn() ) ).withRel( "isbn" ),
            linkTo( methodOn( BooksController.class ).getByTitle( book.getTitle() ) ).withRel( "title" ),
            linkTo( methodOn( BooksController.class ).update( book.getId(), book ) ).withRel( "update" ),
            linkTo( methodOn( BooksController.class ).delete( book.getId() ) ).withRel( "delete" )
        );
        if( book.getAuthor() != null ){
            model.add( linkTo( methodOn( BooksController.class ).getByAuthor(
                book.getAuthor().getName(), book.getAuthor().getSurnames()
            )).withRel( "author" ) );
        }
        if( book.getPublisher() != null ){
            linkTo( methodOn( BooksController.class ).getByPublisher(
                book.getPublisher().getName()
            )).withRel( "publisher" );
        }
        if( book.getPublished() != null ){
            linkTo( methodOn( BooksController.class ).getByYear(
                book.getPublished().getYear()
            )).withRel( "year" );
        }
        return model;
    }

    @Override
    public CollectionModel<EntityModel<BookDto>> toCollectionModel( Iterable<? extends BookDto> entities ) {
        return RepresentationModelAssembler.super.toCollectionModel( entities );
    }

}
