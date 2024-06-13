package com.jmo.devel.bookstore.api.service;

import com.jmo.devel.bookstore.api.configuration.CacheConfiguration;
import com.jmo.devel.bookstore.api.exception.*;
import com.jmo.devel.bookstore.api.model.Book;
import com.jmo.devel.bookstore.api.repository.AuthorsRepository;
import com.jmo.devel.bookstore.api.repository.BooksRepository;
import com.jmo.devel.bookstore.api.repository.PublishersRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {

    private final BooksRepository      booksRepository;
    private final AuthorsRepository    authorsRepository;
    private final PublishersRepository publishersRepository;

    public BooksService(
        BooksRepository      booksRepository,
        AuthorsRepository    authorsRepository,
        PublishersRepository publishersRepository
    ){
        this.booksRepository      = booksRepository;
        this.authorsRepository    = authorsRepository;
        this.publishersRepository = publishersRepository;
    }

    public List<Book> getAll(){
        List<Book> books = this.booksRepository.findAll();
        if ( !books.isEmpty() ){
            return books;
        }
        throw new NoResultsException( "books", "all" );
    }

    @Cacheable(value = CacheConfiguration.BOOKS_CACHE, key = "#id")
    public Book getById( Long id ){
        return this.booksRepository.findById( id )
            .orElseThrow( () -> new BookNotFoundException( "id", String.valueOf( id ) ) );
    }

    public Book getByTitle( String title ){
        return this.booksRepository.findByTitle( title )
            .orElseThrow( () -> new BookNotFoundException( "title", title ) );
    }

    public List<Book> getByTitleLike( String title ){
        List<Book> books = this.booksRepository
            .findByTitleContainingIgnoreCase( title );
        if ( !books.isEmpty() ){
            return books;
        }
        throw new NoResultsException( "title", title );
    }

    public Book getByIsbn( String isbn ){
        return this.booksRepository.findByIsbn( isbn )
            .orElseThrow( () -> new BookNotFoundException( "isbn", isbn ) );
    }

    public List<Book> getByPages( int min, int max ){
        max = (max == -1)? Integer.MAX_VALUE : max;
        List<Book> books = this.booksRepository
            .findByPagesBetween( min, max );
        if ( !books.isEmpty() ){
            return books;
        }
        throw new NoResultsException( "pages", min + "-" + max );
    }

    public List<Book> getByYear( int year ){
        List<Book> books = this.booksRepository
            .findByPublishedYear( year );
        if ( !books.isEmpty() ){
            return books;
        }
        throw new NoResultsException( "year", String.valueOf( year ) );
    }

    public List<Book> getByPublisher( String publisher ){
        List<Book> books = this.booksRepository
            .findByPublisherName( publisher );
        if ( !books.isEmpty() ){
            return books;
        }
        throw new NoResultsException( "publisher", publisher );
    }

    public List<Book> getByAuthor( String name, String surnames ){
        List<Book> books = this.booksRepository
            .findByAuthorNameAndAuthorSurnames( name, surnames );
        if ( !books.isEmpty() ){
            return books;
        }
        throw new NoResultsException( "author", name + " " + surnames );
    }

    public Book create( Book book ){
        Optional<Book> existing = booksRepository.findByIsbn( book.getIsbn() );
        if( existing.isPresent() ){
            throw new ExistingBookException( book.getIsbn() );
        }
        if( book.getPublisher() != null ){
            book.setPublisher(
                this.publishersRepository.findById( book.getPublisher().getId() )
                    .orElseThrow( () ->
                        new PublisherNotFoundException( "id", String.valueOf( book.getPublisher().getId() ) )
                    )
            );
        }
        if( book.getAuthor() != null ){
            book.setAuthor(
                this.authorsRepository.findById( book.getAuthor().getId() )
                    .orElseThrow( () ->
                        new AuthorNotFoundException( "id", String.valueOf( book.getAuthor().getId() ) )
                    )
            );
        }
        return booksRepository.save( book );
    }

    public Book update( Long id, Book book ){
        Optional<Book> existing = booksRepository.findById( id );
        if( existing.isEmpty() ){
            throw new BookNotFoundException( "id", String.valueOf( id ) );
        }
        book.setId( id );
        return booksRepository.save( book );
    }

    public void delete( Long id ){
        Optional<Book> existing = booksRepository.findById( id );
        if( existing.isEmpty() ){
            throw new BookNotFoundException( "id", String.valueOf( id ) );
        }
        booksRepository.deleteById( id );
    }

}
