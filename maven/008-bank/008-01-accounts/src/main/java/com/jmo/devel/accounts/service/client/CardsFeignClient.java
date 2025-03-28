package com.jmo.devel.accounts.service.client;

import com.jmo.devel.accounts.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( "cards" )
public interface CardsFeignClient {

    @GetMapping( value = "/api/fetch" )
    public ResponseEntity<CardDto> fetchCardDetails(
        @RequestHeader("correlation-id") String correlationId,
        @RequestParam String mobileNumber
    );

}
