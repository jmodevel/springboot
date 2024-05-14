package com.jmo.devel.bookstore.admin.util.search;


import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authors", url = "${authors.service.url}")
public interface AuthorsSearchClient {

    @GetMapping("/name/{name}")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getByName(
        @PathVariable( "name" ) String name
    );

    @GetMapping("/surnames/{surnames}")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getBySurnames(
        @PathVariable( "surnames" ) String surnames
    );

    @GetMapping( "/surnames-like/{surnames}" )
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getBySurnamesLike(
        @PathVariable( "surnames" ) String surnames
    );

    @GetMapping( "/alive")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getAlive();

    @GetMapping( "/born/{year}")
    ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> getByBirthDateAfter(
        @PathVariable int year
    );

}
