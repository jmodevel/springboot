package com.jmo.devel.bookstore.admin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import com.jmo.devel.bookstore.admin.dto.BookDto;
import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import com.jmo.devel.bookstore.admin.util.search.AuthorsSearchClient;
import com.jmo.devel.bookstore.admin.util.search.BooksSearchClient;
import com.jmo.devel.bookstore.admin.util.search.PublishersSearchClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SearchService {

    private final AuthorsSearchClient    authorsSearchClient;
    private final BooksSearchClient      booksSearchClient;
    private final PublishersSearchClient publishersSearchClient;
    private final ObjectMapper           objectMapper;

    public SearchService(
        AuthorsSearchClient    authorsSearchClient,
        BooksSearchClient      booksSearchClient,
        PublishersSearchClient publishersSearchClient,
        ObjectMapper           objectMapper
    ){
        this.authorsSearchClient    = authorsSearchClient;
        this.booksSearchClient      = booksSearchClient;
        this.publishersSearchClient = publishersSearchClient;
        this.objectMapper           = objectMapper;
    }

    public List<BookDto> getBooksByTitle( String title ){
        return getBooks( this.booksSearchClient.getByTitleLike( title ) );
    }

    public List<BookDto> getBooksByPages( int min, int max ){
        return getBooks( this.booksSearchClient.getByPages( min, max ) );
    }

    public List<BookDto> getBooksByYear( int year ){
        return getBooks( this.booksSearchClient.getByYear( year ) );
    }

    public List<BookDto> getBooksByPublisher( String publisher ){
        return getBooks( this.booksSearchClient.getByPublisher( publisher ) );
    }

    public List<BookDto> getBooksByAuthor( String name, String surnames ){
        return getBooks( this.booksSearchClient.getByAuthor( name, surnames ) );
    }

    public List<AuthorDto> getAuthorsByName( String name ){
        return getAuthors( this.authorsSearchClient.getByName( name ) );
    }

    public List<AuthorDto> getAuthorsBySurnames( String surnames ){
        return getAuthors( this.authorsSearchClient.getBySurnames( surnames ) );
    }

    public List<AuthorDto> getAuthorsBySurnamesLike( String surnames ){
        return getAuthors( this.authorsSearchClient.getBySurnamesLike( surnames ) );
    }

    public List<AuthorDto> getAuthorsAlive(){
        return getAuthors( this.authorsSearchClient.getAlive() );
    }

    public List<AuthorDto> getAuthorsBornAfter( int year ){
        return getAuthors( this.authorsSearchClient.getByBirthDateAfter( year ) );
    }

    public List<PublisherDto> getPublishersByHeadquarters( List<String> heardquarters ){
        return getPublishers( this.publishersSearchClient.getByHeadquarters( heardquarters ) );
    }

    private List<BookDto> getBooks(
        ResponseEntity<CollectionModel<EntityModel<BookDto>>> response
    ){
        CollectionModel<EntityModel<BookDto>> body = response.getBody();
        Collection<BookDto> books = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ) :
            List.of();
        return books.stream().toList();

    }

    private List<AuthorDto> getAuthors(
        ResponseEntity<CollectionModel<EntityModel<AuthorDto>>> response
    ){
        CollectionModel<EntityModel<AuthorDto>> body = response.getBody();
        Collection<AuthorDto> authors = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ):
            List.of();
        return authors.stream().toList();
    }

    private List<PublisherDto> getPublishers(
        ResponseEntity<CollectionModel<EntityModel<PublisherDto>>> response
    ){
        CollectionModel<EntityModel<PublisherDto>> body = response.getBody();
        Collection<PublisherDto> publishers = ( body != null )?
            objectMapper.convertValue( body.getContent(), new TypeReference<>(){} ) :
            List.of();
        return publishers.stream().toList();
    }

}
