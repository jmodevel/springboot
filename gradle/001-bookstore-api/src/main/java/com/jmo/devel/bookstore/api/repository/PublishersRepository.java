package com.jmo.devel.bookstore.api.repository;

import com.jmo.devel.bookstore.api.model.Publisher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublishersRepository extends CrudRepository<Publisher, Long> {

    @Query( "select p from Publisher p where p.name = :name")
    Optional<Publisher> findByName( String name );
    @Query( "select p from Publisher p where p.headquarters in :locations")
    List<Publisher> findByHeadquartersIn( List<String> locations );

}
