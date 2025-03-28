package com.jmo.devel.accounts.service.client;

import com.jmo.devel.accounts.dto.CardDto;
import com.jmo.devel.accounts.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient( "loans" )
public interface LoansFeignClient {

    @GetMapping( value = "/api/fetch" )
    public ResponseEntity<LoanDto> fetchLoanDetails( @RequestParam String mobileNumber );

}
