package com.jmo.devel.bookstore.api.service;

import com.jmo.devel.bookstore.api.exception.ExistingPublisherException;
import com.jmo.devel.bookstore.api.exception.NoResultsException;
import com.jmo.devel.bookstore.api.exception.PublisherNotFoundException;
import com.jmo.devel.bookstore.api.model.Publisher;
import com.jmo.devel.bookstore.api.repository.PublishersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublishersService {

    private PublishersRepository publishersRepository;

    public PublishersService( PublishersRepository publishersRepository ){
        this.publishersRepository = publishersRepository;
    }

    public Publisher getById(Long id ){
        return this.publishersRepository.findById( id )
            .orElseThrow( () -> new PublisherNotFoundException( "id", String.valueOf( id ) ) );
    }

    public Publisher getByName( String name ){
        return this.publishersRepository.findByName( name )
            .orElseThrow( () -> new PublisherNotFoundException( "name", name ) );
    }

    public List<Publisher> getByHeadquarters( List<String> headquarters ){
        List<Publisher> publishers = this.publishersRepository
            .findByHeadquartersIn( headquarters );
        if ( !publishers.isEmpty() ){
            return publishers;
        }
        throw new NoResultsException( "headquarters",
            String.join( ",", headquarters ) );
    }

    public Publisher create( Publisher publisher ){
        Optional<Publisher> existing = publishersRepository
            .findByName( publisher.getName() );
        if( existing.isPresent() ){
            throw new ExistingPublisherException(
                publisher.getName() );
        }
        return publishersRepository.save( publisher );
    }

    public Publisher update( Long id, Publisher publisher ){
        Optional<Publisher> existing = publishersRepository.findById( id );
        if( existing.isEmpty() ){
            throw new PublisherNotFoundException( "id", String.valueOf( id ) );
        }
        publisher.setId( id );
        return publishersRepository.save( publisher );
    }

    public void delete( Long id ){
        Optional<Publisher> existing = publishersRepository.findById( id );
        if( existing.isEmpty() ){
            throw new PublisherNotFoundException( "id", String.valueOf( id ) );
        }
        publishersRepository.deleteById( id );
    }

}
