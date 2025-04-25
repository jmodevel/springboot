package com.jmo.devel.accounts.controller;

import com.jmo.devel.accounts.constants.AccountsConstants;
import com.jmo.devel.accounts.dto.ContactInfoDto;
import com.jmo.devel.accounts.dto.CustomerDto;
import com.jmo.devel.accounts.dto.ResponseDto;
import com.jmo.devel.accounts.service.IAccountsService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountsController {

    private static final Logger logger = LoggerFactory.getLogger( AccountsController.class );

    private final IAccountsService accountsService;

    @Value( "${build.version}" )
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private ContactInfoDto contactInfoDto;

    public AccountsController( IAccountsService accountsService ) {
        this.accountsService = accountsService;
    }

    @Operation(
        summary = "Create Account REST API",
        description = "REST API to create account and customer inside bank"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Account created successfully"
    )
    @PostMapping( "/create" )
    public ResponseEntity<ResponseDto> createAccount( @Valid @RequestBody CustomerDto customerDto ){
        accountsService.createAccount( customerDto );
        return ResponseEntity
            .status( HttpStatus.CREATED )
            .body(
                new ResponseDto( HttpStatus.CREATED, AccountsConstants.MESSAGE_201 )
            );
    }

    @Operation(
        summary = "Fetch Account DetailsREST API",
        description = "REST API to fetch account and customer details based on a mobile number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Account details fetched successfully"
    )
    @GetMapping( "/fetch" )
    public ResponseEntity<CustomerDto> fetchAccountDetails( @RequestParam
                                                                @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
                                                                String mobileNumber ){
        return ResponseEntity
            .status( HttpStatus.OK )
            .body( accountsService.fetchAccount( mobileNumber ) );
    }

    @Operation(
        summary = "Update Account Details REST API",
        description = "REST API to update account and customer details based on a customer"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account updated successfully"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Update failed"
        )
    })
    @PutMapping( "/update" )
    public ResponseEntity<ResponseDto> updateAccountDetails( @Valid @RequestBody CustomerDto customerDto ){
        boolean updated = accountsService.updateAccount( customerDto );
        if( updated ) {
            return ResponseEntity
                .status( HttpStatus.OK )
                .body( new ResponseDto( HttpStatus.OK, AccountsConstants.MESSAGE_200 ) );
        }
        return ResponseEntity
            .status( HttpStatus.EXPECTATION_FAILED )
            .body( new ResponseDto( HttpStatus.EXPECTATION_FAILED, AccountsConstants.MESSAGE_417_UPDATE ) );
    }

    @Operation(
        summary = "Delete Account Details REST API",
        description = "REST API to delete account and customer details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account deleted successfully"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Delete failed"
        )
    })
    @DeleteMapping( "/delete" )
    public ResponseEntity<ResponseDto> deleteAccountDetails( @RequestParam
                                                                 @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
                                                                 String mobileNumber ){
        boolean deleted = accountsService.deleteAccount( mobileNumber );
        if ( deleted ) {
            return ResponseEntity
                .status( HttpStatus.OK )
                .body( new ResponseDto( HttpStatus.OK, AccountsConstants.MESSAGE_200 ) );
        }
        return ResponseEntity
            .status( HttpStatus.EXPECTATION_FAILED )
            .body( new ResponseDto( HttpStatus.EXPECTATION_FAILED, AccountsConstants.MESSAGE_417_DELETE ) );
    }

    @Operation(
        summary = "Get build information",
        description = "Get build information for the accounts microservice"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Build information successfully retrieved"
    )
    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    @GetMapping( "/build-info" )
    public ResponseEntity<String> getBuildInfo() {
        logger.debug( "--getBuildInfo: invoked" );
        return ResponseEntity.ok( buildVersion );
    }

    public ResponseEntity<String> getBuildInfoFallback( Throwable th ){
        logger.debug( "--getBuildInfoFallback: invoked" );
        return ResponseEntity.ok( "0.9" );
    }

    @Operation(
        summary = "Get Java version",
        description = "Get Java version for the accounts microservice"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Java version successfully retrieved"
    )
    @GetMapping( "/java-version" )
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.ok( environment.getProperty( "java.version" ) );
    }

    @Operation(
        summary = "Get contact information",
        description = "Get contact info for the accounts microservice"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Contact info successfully retrieved"
    )
    @GetMapping( "/contact-info" )
    public ResponseEntity<ContactInfoDto> getContactInfo() {
        return ResponseEntity.ok( contactInfoDto );
    }

}
