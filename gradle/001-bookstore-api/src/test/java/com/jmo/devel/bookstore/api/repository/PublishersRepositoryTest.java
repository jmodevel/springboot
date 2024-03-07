package com.jmo.devel.bookstore.api.repository;

import com.jmo.devel.bookstore.api.model.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PublishersRepositoryTest {

    @Autowired
    private PublishersRepository repository;

    @Test
    void givenOnePublisher_whenFindAll_thenPublisherIsReturned(){
        repository.save(
            Publisher.builder()
                .name( "Planeta" )
                .headquarters( "Barcelona" )
                .website( "https://www.planeta.es/es" )
                .build()
        );
        Iterable<Publisher> all = repository.findAll();
        assertThat( all )
            .hasSizeGreaterThan(0)
            .anyMatch( p -> p.getName().equals( "Planeta" ) );
    }

    @Test
    void givenExistingPublisherName_whenFindByName_thenPublisherIsReturned(){
        repository.save(
            Publisher.builder()
                .name( "Planeta" )
                .headquarters( "Barcelona" )
                .website( "https://www.planeta.es/es" )
                .build()
        );
        assertThat( repository.findByName( "Planeta" ) ).isPresent();
    }

    @Test
    void givenTwoPublishers_whenFindByHeadquarters_thenPublishersAreReturned(){
        repository.saveAll(
            List.of(
                Publisher.builder()
                    .name( "Planeta" )
                    .headquarters( "Barcelona" )
                    .website( "https://www.planeta.es/es" )
                    .build(),
                Publisher.builder()
                    .name( "Blackie Books" )
                    .headquarters( "Barcelona" )
                    .website( "https://blackiebooks.org/" )
                    .build()
            ));
        List<Publisher> publishers = repository.findByHeadquartersIn( List.of("Barcelona") );
        assertThat( publishers )
            .hasSizeGreaterThanOrEqualTo( 2 )
            .allMatch( p -> p.getHeadquarters().equals( "Barcelona" ) );
    }



}
