package com.jmo.devel.bookstore.admin.controller;

import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import com.jmo.devel.bookstore.admin.dto.BookDto;
import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import com.jmo.devel.bookstore.admin.service.AuthorsService;
import com.jmo.devel.bookstore.admin.service.BooksService;
import com.jmo.devel.bookstore.admin.service.PublishersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping( "/books" )
public class BooksController {

    public static final String REDIRECT_BOOKS = "redirect:/books";

    public static final String BOOKS      = "books";
    public static final String BOOK       = "book";
    public static final String AUTHORS    = "authors";
    public static final String PUBLISHERS = "publishers";

    private final BooksService      service;
    private final AuthorsService    authorsService;
    private final PublishersService publishersService;

    public BooksController(
        BooksService      service,
        AuthorsService    authorsService,
        PublishersService publishersService
    ){
        this.service           = service;
        this.authorsService    = authorsService;
        this.publishersService = publishersService;
    }

    @GetMapping("/add")
    public String showCreationForm( Model model ){
        List<AuthorDto>    authors    = this.authorsService.getAll();
        List<PublisherDto> publishers = this.publishersService.getAll();
        BookDto book = new BookDto();
        model.addAttribute( BOOK,       book       );
        model.addAttribute( AUTHORS,    authors    );
        model.addAttribute( PUBLISHERS, publishers );
        return "books/add";
    }

    @PostMapping
    public String create( @ModelAttribute(BOOK) BookDto book ) {
        book.setPublisher( this.publishersService.get( book.getPublisher().getId() ) );
        book.setAuthor   ( this.authorsService   .get( book.getAuthor()   .getId() ) );
        this.service.create( book );
        return REDIRECT_BOOKS;
    }

    @GetMapping( { "", "/" } )
    public String list( Model model ){
        List<BookDto> books = this.service.getAll();
        model.addAttribute( BOOKS, books );
        return "books/list-all";
    }

    @GetMapping("/{id}")
    public String showViewForm( @PathVariable Long id, Model model ){
        BookDto book = this.service.get( id );
        model.addAttribute( BOOK, book );
        return "books/view";
    }

    @GetMapping("/search")
    public String showSearchForm( Model model ){
        List<PublisherDto> publishers = this.publishersService.getAll();
        model.addAttribute( BOOKS,      List.of()  );
        model.addAttribute( PUBLISHERS, publishers );
        return "books/search";
    }

    @GetMapping("/{id}/edit")
    public String showEditionForm( @PathVariable Long id, Model model ){
        BookDto            book       = this.service.get( id );
        List<AuthorDto>    authors    = this.authorsService.getAll();
        List<PublisherDto> publishers = this.publishersService.getAll();
        model.addAttribute( BOOK,       book       );
        model.addAttribute( AUTHORS,    authors    );
        model.addAttribute( PUBLISHERS, publishers );
        return "books/edit";
    }

    @PutMapping( "/{id}" )
    public String update(
        @PathVariable Long id,
        @ModelAttribute(BOOK) BookDto book
    ){
        BookDto existing = this.service.get( id );
        existing.setIsbn     ( book.getIsbn() );
        existing.setTitle    ( book.getTitle() );
        existing.setPages    ( book.getPages() );
        existing.setPublished( book.getPublished() );
        existing.setPublisher( this.publishersService.get( book.getPublisher().getId() ) );
        existing.setAuthor   ( this.authorsService   .get( book.getAuthor()   .getId() ) );
        this.service.update( id, existing );
        return REDIRECT_BOOKS;
    }

    @DeleteMapping( "/{id}" )
    public String delete( @PathVariable Long id ){
        this.service.delete( id );
        return REDIRECT_BOOKS;
    }

}
