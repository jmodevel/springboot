package com.jmo.devel.bookstore.admin.controller;

import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import com.jmo.devel.bookstore.admin.dto.BookDto;
import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import com.jmo.devel.bookstore.admin.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping( "/search" )
public class SearchController {

    public static final String ACTION        = "action";
    public static final String CRITERIA_1    = "criteria1";
    public static final String CRITERIA_2    = "criteria2";
    public static final String BOOKS         = "books";
    public static final String PAGES         = "pages";
    public static final String AUTHOR        = "author";
    public static final String TITLE         = "title";
    public static final String YEAR          = "year";
    public static final String PUBLISHER     = "publisher";
    public static final String QUERY         = "query";
    public static final String AUTHORS       = "authors";
    public static final String ALIVE         = "alive";
    public static final String NAME          = "name";
    public static final String SURNAMES      = "surnames";
    public static final String SURNAMES_LIKE = "surnamesLike";
    public static final String BORN_AFTER    = "bornAfter";
    public static final String PUBLISHERS    = "publishers";
    public static final String HEADQUARTERS  = "headquarters";

    private final SearchService service;

    public SearchController( SearchService service ){
        this.service = service;
    }

    @GetMapping( "/books" )
    public String searchBooks(
        @RequestParam(ACTION) String action,
        @RequestParam(CRITERIA_1) String criteria1,
        @RequestParam( value = CRITERIA_2, required = false ) String criteria2,
        Model model
    ) {
        if( action == null || action.trim().isEmpty() ){
            model.addAttribute( BOOKS, List.of() );
        } else {
            if( action.equals( PAGES ) &&
                ( criteria1 == null || criteria1.trim().isEmpty() )
            ){
                criteria1 = "0";
            }
            if( ( criteria1 == null || criteria1.trim().isEmpty() ) ||
                ( ( action.equals( AUTHOR ) || action.equals( PAGES ) ) &&
                    ( criteria2 == null || criteria2.trim().isEmpty() )
                )
            ){
                model.addAttribute( BOOKS, List.of() );
            } else {
                List<BookDto> books = switch( action ){
                    case TITLE     -> service.getBooksByTitle( criteria1 );
                    case PAGES     -> service.getBooksByPages( Integer.parseInt( criteria1 ), Integer.parseInt( criteria2 ) );
                    case YEAR      -> service.getBooksByYear( Integer.parseInt( criteria1 ) );
                    case PUBLISHER -> service.getBooksByPublisher( criteria1 );
                    case AUTHOR    -> service.getBooksByAuthor( criteria1, criteria2 );
                    default        -> List.of();
                };
                model.addAttribute( BOOKS, books );
            }
        }
        return "books/search-results :: searchResults";
    }

    @GetMapping( "/authors" )
    public String searchAuthors(
        @RequestParam(ACTION) String action,
        @RequestParam(QUERY) String query,
        Model model
    ) {
        if( action == null || action.trim().isEmpty() ){
            model.addAttribute( AUTHORS, List.of() );
        } else {
            if( !action.equals( ALIVE ) &&
                ( query  == null || query .trim().isEmpty() ) ){
                model.addAttribute( AUTHORS, List.of() );
            } else {
                List<AuthorDto> authors = switch( action ) {
                    case NAME          -> service.getAuthorsByName(query);
                    case SURNAMES      -> service.getAuthorsBySurnames(query);
                    case SURNAMES_LIKE -> service.getAuthorsBySurnamesLike(query);
                    case ALIVE         -> service.getAuthorsAlive();
                    case BORN_AFTER    -> service.getAuthorsBornAfter(Integer.parseInt(query));
                    default            -> List.of();
                };
                model.addAttribute( AUTHORS, authors );
            }
        }
        return "authors/search-results :: searchResults";
    }

    @GetMapping( "/publishers" )
    public String searchPublishers(
        @RequestParam(ACTION) String action,
        @RequestParam(QUERY) String query,
        Model model
    ) {
        if( action == null || action.trim().isEmpty() ){
            model.addAttribute( PUBLISHERS, List.of() );
        } else {
            if( query == null || query.trim().isEmpty() ){
                model.addAttribute( PUBLISHERS, List.of() );
            } else {
                List<String> headquarters = Arrays.stream( query.split("," ) )
                    .map(String::trim)
                    .toList();
                List<PublisherDto> authors =
                    ( action.equals( HEADQUARTERS ) ) ? service.getPublishersByHeadquarters( headquarters ) :
                                                  List.of();
                model.addAttribute( PUBLISHERS, authors );
            }
        }
        return "publishers/search-results :: searchResults";
    }

}
