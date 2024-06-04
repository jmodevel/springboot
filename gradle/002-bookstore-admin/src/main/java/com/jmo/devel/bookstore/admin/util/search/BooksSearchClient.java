package com.jmo.devel.bookstore.admin.util.search;


import com.jmo.devel.bookstore.admin.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bookstore-api", contextId = "books")
public interface BooksSearchClient {

    @GetMapping( "/books/title-like/{title}" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByTitleLike(
        @PathVariable( "title" ) String title
    );

    @GetMapping( "/books/pages" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByPages(
        @RequestParam( "min" ) int min,
        @RequestParam( "max" ) int max
    );

    @GetMapping( "/books/year/{year}" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByYear(
        @PathVariable("year") int year
    );

    @GetMapping( "/books/publisher/{publisher}" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByPublisher(
        @PathVariable("publisher") String publisher
    );

    @GetMapping( "/books/author" )
    ResponseEntity<CollectionModel<EntityModel<BookDto>>> getByAuthor(
        @RequestParam( "name"     ) String name,
        @RequestParam( "surnames" ) String surnames
    );

}
