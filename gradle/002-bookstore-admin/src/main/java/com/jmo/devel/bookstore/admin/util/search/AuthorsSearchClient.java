package com.jmo.devel.bookstore.admin.util.search;


import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bookstore-api", contextId = "authors")
public interface AuthorsSearchClient {

    @GetMapping("/authors/name/{name}")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getByName(
        @PathVariable( "name" ) String name
    );

    @GetMapping("/authors/surnames/{surnames}")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getBySurnames(
        @PathVariable( "surnames" ) String surnames
    );

    @GetMapping( "/authors/surnames-like/{surnames}" )
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getBySurnamesLike(
        @PathVariable( "surnames" ) String surnames
    );

    @GetMapping( "/authors/alive")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getAlive();

    @GetMapping( "/authors/born/{year}")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getByBirthDateAfter(
        @PathVariable int year
    );

}
