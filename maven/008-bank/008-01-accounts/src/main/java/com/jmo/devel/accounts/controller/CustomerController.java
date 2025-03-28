package com.jmo.devel.accounts.controller;

import com.jmo.devel.accounts.constants.AccountsConstants;
import com.jmo.devel.accounts.dto.ContactInfoDto;
import com.jmo.devel.accounts.dto.CustomerDetailsDto;
import com.jmo.devel.accounts.dto.CustomerDto;
import com.jmo.devel.accounts.dto.ResponseDto;
import com.jmo.devel.accounts.service.IAccountsService;
import com.jmo.devel.accounts.service.ICustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "REST API for Customers Microservice",
    description = "REST APIs to fetch Customer Details"
)
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger( CustomerController.class );
    private ICustomersService customersService;

    public CustomerController( ICustomersService customersService ) {
        this.customersService = customersService;
    }

    @Operation(
        summary = "Fetch Customer DetailsREST API",
        description = "REST API to fetch customer details based on a mobile number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Customer details fetched successfully"
    )
    @GetMapping( "/fetchCustomerDetails" )
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails( @RequestHeader("correlation-id") String correlationId,
                                                                    @RequestParam
                                                                    @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
                                                                    String mobileNumber ){
        logger.debug( "correlation-id found: {}", correlationId );
        CustomerDetailsDto customerDetailsDto = this.customersService.fetchCustomerDetails( mobileNumber, correlationId );
        return ResponseEntity.status( HttpStatus.OK ).body( customerDetailsDto );
    }
}
