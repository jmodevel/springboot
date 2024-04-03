package com.jmo.devel.bookstore.api.assembler;

import com.jmo.devel.bookstore.api.controller.PublishersController;
import com.jmo.devel.bookstore.api.dto.PublisherDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PublishersModelAssembler implements RepresentationModelAssembler<PublisherDto, EntityModel<PublisherDto>> {

    @Override
    public EntityModel<PublisherDto> toModel( PublisherDto publisher ) {
        return EntityModel.of(
            publisher,
            linkTo( methodOn( PublishersController.class ).getById( publisher.getId() ) ).withSelfRel(),
            linkTo( methodOn( PublishersController.class ).getByName( publisher.getName() ) ).withRel( "name" ),
            linkTo( methodOn( PublishersController.class ).update( publisher.getId(), publisher ) ).withRel( "update" ),
            linkTo( methodOn( PublishersController.class ).delete( publisher.getId() ) ).withRel( "delete" )
        );
    }

    @Override
    public CollectionModel<EntityModel<PublisherDto>> toCollectionModel( Iterable<? extends PublisherDto> entities ) {
        CollectionModel<EntityModel<PublisherDto>> model = RepresentationModelAssembler.super.toCollectionModel(entities);
        model.add(
            linkTo( methodOn( PublishersController.class ).getByHeadquarters(
                StreamSupport.stream( entities.spliterator(), false )
                    .map( PublisherDto::getHeadquarters ).distinct().toList()
            )).withRel( "headquarters" )
        );
        return model;
    }

}
