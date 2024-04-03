package com.jmo.devel.bookstore.api.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "publishers", itemRelation = "publisher")
public class PublisherDto extends RepresentationModel<PublisherDto> {

    private Long          id;
    private String        name;
    private String        headquarters;
    private String        website;
    private List<BookDto> books;

}
