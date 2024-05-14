package com.jmo.devel.bookstore.admin.controller;

import com.jmo.devel.bookstore.admin.dto.AuthorDto;
import com.jmo.devel.bookstore.admin.dto.BookDto;
import com.jmo.devel.bookstore.admin.service.AuthorsService;
import com.jmo.devel.bookstore.admin.service.BooksService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping( "/authors" )
public class AuthorsController {

    public static final String REDIRECT_AUTHORS = "redirect:/authors";

    public static final String AUTHORS = "authors";
    public static final String AUTHOR  = "author";
    public static final String BOOKS   = "books";

    private final AuthorsService service;
    private final BooksService   booksService;

    public AuthorsController( AuthorsService service, BooksService booksService ){
        this.service      = service;
        this.booksService = booksService;
    }

    @GetMapping("/add")
    public String showCreationForm( Model model ){
        AuthorDto author = new AuthorDto();
        model.addAttribute( AUTHOR, author );
        return "authors/add";
    }

    @PostMapping
    public String create( @ModelAttribute(AUTHOR) AuthorDto author ) {
        this.service.create( author );
        return REDIRECT_AUTHORS;
    }

    @GetMapping( { "", "/" } )
    public String list( Model model ){
        List<AuthorDto> authors = this.service.getAll();
        model.addAttribute( AUTHORS, authors );
        return "authors/list-all";
    }

    @GetMapping("/{id}")
    public String showViewForm( @PathVariable Long id, Model model ){
        AuthorDto     author = this.service.get( id );
        List<BookDto> books  = this.booksService.getByAuthor( author );
        model.addAttribute( AUTHOR, author );
        model.addAttribute( BOOKS,  books  );
        return "authors/view";
    }

    @GetMapping("/search")
    public String showSearchForm( Model model ){
        model.addAttribute( AUTHORS, List.of() );
        return "authors/search";
    }

    @GetMapping("/{id}/edit")
    public String showEditionForm( @PathVariable Long id, Model model ){
        AuthorDto author = this.service.get( id );
        model.addAttribute( AUTHOR, author );
        return "authors/edit";
    }

    @PutMapping( "/{id}" )
    public String update(
        @PathVariable Long id,
        @ModelAttribute(AUTHOR) AuthorDto author
    ){
        AuthorDto existing = this.service.get( id );
        existing.setName        ( author.getName() );
        existing.setSurnames    ( author.getSurnames() );
        existing.setBirthDate   ( author.getBirthDate() );
        existing.setDeath       ( author.getDeath() );
        this.service.update( id, existing );
        return REDIRECT_AUTHORS;
    }

    @DeleteMapping( "/{id}" )
    public String delete( @PathVariable Long id ){
        this.service.delete( id );
        return REDIRECT_AUTHORS;
    }

}
