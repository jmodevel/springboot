package com.jmo.devel.tv.shows.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmo.devel.tv.shows.repository.ShowsRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@Configuration
@Slf4j
public class InitialLoad {

    @Value("${tv.shows.app.initial.load.file}")
    private Resource[] testData;

    private final ShowsRepository showsRepository;

    public InitialLoad(ShowsRepository showsRepository){
        this.showsRepository = showsRepository;
    }

    @PostConstruct
    public void cleanShows(){
        this.showsRepository.deleteAll();
        log.info( "Existing tests TV shows cleaned" );
    }

    @Bean
    @Autowired
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator( ObjectMapper objectMapper ) {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setMapper( objectMapper );
        factory.setResources( testData );
        return factory;
    }

}
