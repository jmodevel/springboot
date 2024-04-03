package com.jmo.devel.bookstore.api.assembler;

import com.jmo.devel.bookstore.api.controller.AuthorsController;
import com.jmo.devel.bookstore.api.dto.AuthorDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthorsModelAssembler implements RepresentationModelAssembler<AuthorDto, EntityModel<AuthorDto>> {

    @Override
    public EntityModel<AuthorDto> toModel( AuthorDto author ) {
        EntityModel<AuthorDto> entity = EntityModel.of(
            author,
            linkTo( methodOn( AuthorsController.class ).getById( author.getId() ) ).withSelfRel(),
            linkTo( methodOn( AuthorsController.class ).getByNameAndSurnames(
                author.getName(), author.getSurnames()
            )).withRel( "name+surnames" ),
            linkTo( methodOn( AuthorsController.class ).getByName( author.getName() ) ).withRel( "name" ),
            linkTo( methodOn( AuthorsController.class ).getBySurnames(
                author.getSurnames()
            )).withRel( "surnames" ),
            linkTo( methodOn( AuthorsController.class ).update( author.getId(), author ) ).withRel( "update" ),
            linkTo( methodOn( AuthorsController.class ).delete( author.getId() ) ).withRel( "delete" )
        );
        if( author.getBirthDate() != null ){
            linkTo( methodOn( AuthorsController.class ).getByBirthDateAfter(
                author.getBirthDate().getYear()
            )).withRel( "birthdate" );
        }
        return entity;
    }

    @Override
    public CollectionModel<EntityModel<AuthorDto>> toCollectionModel( Iterable<? extends AuthorDto> entities ) {
        CollectionModel<EntityModel<AuthorDto>> model = RepresentationModelAssembler.super.toCollectionModel( entities );
        model.add( linkTo( methodOn( AuthorsController.class ).getAlive() ).withRel( "alive" ) );
        return model;
    }

}
