package com.jmo.devel.tv.shows.repository;

import com.jmo.devel.tv.shows.model.Show;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
class ShowsRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ShowsRepository repository;

    @Test
    void givenShowInRepository_when() {
        var show = Show.builder()
                .name( "Barry" )
                .genres( new String[]{"Comedy", "Drama"} )
                .type( "Scripted" )
                .runtime( 30 )
                .build();
        mongoTemplate.insert( show );
        Show barry = repository.findItemByName( "Barry" ).orElse( new Show() );
        Assertions.assertEquals( "Barry", barry.getName() );
    }

    @AfterEach
    void cleanUpDatabase() {
        mongoTemplate.getDb().drop();
    }

}
