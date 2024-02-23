package com.jmo.devel.tv.shows.service;

import com.jmo.devel.tv.shows.exception.NoResultsException;
import com.jmo.devel.tv.shows.model.Show;
import com.jmo.devel.tv.shows.repository.ShowsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest(classes = {ShowsService.class})
class ShowsServiceTest {

    @MockBean
    ShowsRepository repository;

    @Autowired
    ShowsService service;

    @Test
    void givenAnExistingType_whenFindIsRequested_thenResultsAreReturned(){
        Mockito.when( repository.findByTypeOrderByNameAsc( "Scripted" ) )
                .thenReturn( List.of( new Show() ) );
        Assertions.assertThat( service.findByType( "Scripted" ) )
                .hasSize( 1 );
    }

    @Test
    void givenAnNonExistingType_whenFindIsRequested_thenNoResultsExceptionIsThrown(){
        Mockito.when( repository.findByTypeOrderByNameAsc( "Scripted" ) )
                .thenReturn( List.of() );
        Assertions.assertThatExceptionOfType( NoResultsException.class )
                .isThrownBy( () -> service.findByType( "Scripted" ) );
    }

}
