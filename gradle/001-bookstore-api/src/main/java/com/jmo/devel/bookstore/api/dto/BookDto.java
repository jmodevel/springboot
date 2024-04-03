package com.jmo.devel.bookstore.api.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "books", itemRelation = "book")
public class BookDto extends RepresentationModel<BookDto> {

    private Long         id;
    private String       title;
    private String       isbn;
    private int          pages;
    private LocalDate    published;
    private PublisherDto publisher;
    private AuthorDto    author;

}
