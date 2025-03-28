package com.jmo.devel.accounts.service.client;

import com.jmo.devel.accounts.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( "cards" )
public interface CardsFeignClient {

    @GetMapping( value = "/api/fetch" )
    public ResponseEntity<CardDto> fetchCardDetails( @RequestParam String mobileNumber );

}
