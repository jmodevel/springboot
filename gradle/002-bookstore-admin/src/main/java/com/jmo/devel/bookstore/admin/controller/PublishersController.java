package com.jmo.devel.bookstore.admin.controller;

import com.jmo.devel.bookstore.admin.dto.BookDto;
import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import com.jmo.devel.bookstore.admin.service.BooksService;
import com.jmo.devel.bookstore.admin.service.PublishersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping( "/publishers" )
public class PublishersController {

    public static final String REDIRECT_PUBLISHERS = "redirect:/publishers";

    public static final String PUBLISHERS = "publishers";
    public static final String PUBLISHER  = "publisher";
    public static final String BOOKS      = "books";

    private final PublishersService service;
    private final BooksService      booksService;

    public PublishersController( PublishersService service, BooksService booksService ){
        this.service      = service;
        this.booksService = booksService;
    }

    @GetMapping("/add")
    public String showCreationForm( Model model ){
        PublisherDto publisher = new PublisherDto();
        model.addAttribute( PUBLISHER, publisher );
        return "publishers/add";
    }

    @PostMapping
    public String create( @ModelAttribute(PUBLISHER) PublisherDto publisher ) {
        this.service.create( publisher );
        return REDIRECT_PUBLISHERS;
    }

    @GetMapping( { "", "/" } )
    public String list( Model model ){
        List<PublisherDto> publishers = this.service.getAll();
        model.addAttribute( PUBLISHERS, publishers );
        return "publishers/list-all";
    }

    @GetMapping("/{id}")
    public String showViewForm( @PathVariable Long id, Model model ){
        PublisherDto  publisher = this.service.get( id );
        List<BookDto> books     = this.booksService.getByPublisher( publisher );
        model.addAttribute( PUBLISHER, publisher );
        model.addAttribute( BOOKS,     books     );
        return "publishers/view";
    }

    @GetMapping("/search")
    public String showSearchForm( Model model ){
        model.addAttribute( PUBLISHERS, List.of() );
        return "publishers/search";
    }

    @GetMapping("/{id}/edit")
    public String showEditionForm( @PathVariable Long id, Model model ){
        PublisherDto publisher = this.service.get( id );
        model.addAttribute( PUBLISHER, publisher );
        return "publishers/edit";
    }

    @PutMapping( "/{id}" )
    public String update(
        @PathVariable Long id,
        @ModelAttribute(PUBLISHER) PublisherDto publisher
    ){
        PublisherDto existing = this.service.get( id );
        existing.setName        ( publisher.getName() );
        existing.setHeadquarters( publisher.getHeadquarters() );
        existing.setWebsite     ( publisher.getWebsite() );
        this.service.update( id, existing );
        return REDIRECT_PUBLISHERS;
    }

    @DeleteMapping( "/{id}" )
    public String delete( @PathVariable Long id ){
        this.service.delete( id );
        return REDIRECT_PUBLISHERS;
    }

}
