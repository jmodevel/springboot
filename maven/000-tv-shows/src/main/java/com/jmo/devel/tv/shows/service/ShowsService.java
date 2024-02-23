package com.jmo.devel.tv.shows.service;

import com.jmo.devel.tv.shows.exception.NoResultsException;
import com.jmo.devel.tv.shows.exception.ShowNotFoundException;
import com.jmo.devel.tv.shows.model.Show;
import com.jmo.devel.tv.shows.repository.ShowsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowsService {

    private ShowsRepository repository;

    public ShowsService( ShowsRepository repository ){
        this.repository = repository;
    }

    public Show findItemByName( String name ){
        return this.repository.findItemByName( name )
                .orElseThrow( () -> new ShowNotFoundException( name ) );
    }

    public List<Show> findByGenre( String genre ){
        List<Show> shows = this.repository.findByGenre(genre);
        if( shows.isEmpty() ){
            throw new NoResultsException( "genre", genre );
        }
        return shows;
    }

    public List<Show> findByType( String type ){
        List<Show> shows = this.repository.findByTypeOrderByNameAsc(type);
        if( shows.isEmpty() ){
            throw new NoResultsException( "type", type );
        }
        return shows;
    }

    public List<Show> findByStatus( String status ){
        List<Show> shows = this.repository.findByStatusOrderByNameAsc( status );
        if( shows.isEmpty() ){
            throw new NoResultsException( "status", status );
        }
        return shows;
    }

    public List<Show> findByRuntime( int runtime ){
        List<Show> shows = this.repository.findByRuntimeOrderByNameAsc( runtime );
        if( shows.isEmpty() ){
            throw new NoResultsException( "runtime", String.valueOf( runtime ) );
        }
        return shows;
    }

}
