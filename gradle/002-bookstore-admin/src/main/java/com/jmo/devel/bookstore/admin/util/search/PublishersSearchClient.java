package com.jmo.devel.bookstore.admin.util.search;

import com.jmo.devel.bookstore.admin.dto.PublisherDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bookstore-api", contextId = "publishers")
public interface PublishersSearchClient {

    @GetMapping( "/publishers/headquarters" )
    ResponseEntity<CollectionModel<EntityModel<PublisherDto>>> getByHeadquarters(
        @RequestParam( "headquarters" ) List<String> headquarters
    );

}
