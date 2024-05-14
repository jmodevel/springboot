package com.jmo.devel.bookstore.admin.util.search;


import com.jmo.devel.bookstore.admin.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "books", url = "${books.service.url}")
public interface BooksSearchClient {

    @GetMapping( "/title-like/{title}" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByTitleLike(
        @PathVariable( "title" ) String title
    );

    @GetMapping( "/pages" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByPages(
        @RequestParam( "min" ) int min,
        @RequestParam( "max" ) int max
    );

    @GetMapping( "/year/{year}" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByYear(
        @PathVariable("year") int year
    );

    @GetMapping( "/publisher/{publisher}" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByPublisher(
        @PathVariable("publisher") String publisher
    );

    @GetMapping( "/author" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByAuthor(
        @RequestParam( "name"     ) String name,
        @RequestParam( "surnames" ) String surnames
    );

}
