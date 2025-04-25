package com.jmo.devel.accounts.service.client;

import com.jmo.devel.accounts.dto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient{

    @Override
    public ResponseEntity<LoanDto> fetchLoanDetails( String correlationId, String mobileNumber ) {
        return null;
    }

}
