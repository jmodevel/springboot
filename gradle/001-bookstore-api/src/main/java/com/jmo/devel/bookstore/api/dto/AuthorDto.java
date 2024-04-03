package com.jmo.devel.bookstore.api.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "authors", itemRelation = "author")
public class AuthorDto extends RepresentationModel<AuthorDto> {

    private Long          id;
    private String        name;
    private String        surnames;
    private LocalDate     birthDate;
    private LocalDate     death;
    private List<BookDto> books;

}
