package com.jmo.devel.bookstore.api.service;

import com.jmo.devel.bookstore.api.exception.AuthorNotFoundException;
import com.jmo.devel.bookstore.api.exception.ExistingAuthorException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.model.Author;
import com.jmo.devel.bookstore.api.repository.AuthorsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorsService {

    private final AuthorsRepository authorsRepository;

    public AuthorsService( AuthorsRepository authorsRepository ){
        this.authorsRepository = authorsRepository;
    }

    public Author getById( Long id ){
        return this.authorsRepository.findById( id )
            .orElseThrow( () -> new AuthorNotFoundException( "id", String.valueOf(id) ) );
    }

    public List<Author> getByName( String name ){
        List<Author> authors = this.authorsRepository.findByName( name );
        if ( !authors.isEmpty() ){
            return authors;
        }
        throw new NoResultsException( "name", name );
    }

    public List<Author> getBySurnames( String surnames ){
        List<Author> authors = this.authorsRepository.findBySurnames( surnames );
        if ( !authors.isEmpty() ){
            return authors;
        }
        throw new NoResultsException( "surnames", surnames );
    }

    public List<Author> getBySurnamesLike( String surnames ){
        List<Author> authors = this.authorsRepository.findBySurnamesContainingIgnoreCase(
            surnames
        );
        if ( !authors.isEmpty() ){
            return authors;
        }
        throw new NoResultsException( "surnames", surnames );
    }

    public Author getByNameAndSurnames( String name, String surnames ){
        return this.authorsRepository.findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase(
                name, surnames
            ).orElseThrow( () -> new AuthorNotFoundException( "name, surnames", name + " " + surnames ) );
    }

    public List<Author> getAlive(){
        List<Author> authors = this.authorsRepository.findByDeathIsNull();
        if ( !authors.isEmpty() ){
            return authors;
        }
        throw new NoResultsException( "death", "null" );
    }

    public List<Author> getByBirthDateAfter( LocalDate date ){
        List<Author> authors = this.authorsRepository.findByBirthDateAfter( date );
        if ( !authors.isEmpty() ){
            return authors;
        }
        throw new NoResultsException( "date",
            date.format( DateTimeFormatter.ISO_LOCAL_DATE ) );
    }

    public Author create( Author author ){
        Optional<Author> existing = authorsRepository
            .findByNameContainingIgnoreCaseAndSurnamesContainingIgnoreCase(
                author.getName(), author.getSurnames()
            );
        if( existing.isPresent() ){
            throw new ExistingAuthorException(
                author.getName(), author.getSurnames() );
        }
        return authorsRepository.save( author );
    }

    public Author update( Long id, Author author ){
        Optional<Author> existing = authorsRepository.findById( id );
        if( existing.isEmpty() ){
            throw new AuthorNotFoundException( "id", String.valueOf( id ) );
        }
        author.setId( id );
        return authorsRepository.save( author );
    }

    public void delete( Long id ){
        Optional<Author> existing = authorsRepository.findById( id );
        if( existing.isEmpty() ){
            throw new AuthorNotFoundException( "id", String.valueOf( id ) );
        }
        authorsRepository.deleteById( id );
    }

}
