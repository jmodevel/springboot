package com.jmo.devel.loans.controller;

import com.jmo.devel.loans.constants.LoansConstants;
import com.jmo.devel.loans.dto.ContactInfoDto;
import com.jmo.devel.loans.dto.LoanDto;
import com.jmo.devel.loans.dto.ResponseDto;
import com.jmo.devel.loans.service.ILoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
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
public class LoansController {

    private final ILoansService loansService;

    @Value( "${build.version}" )
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private ContactInfoDto contactInfoDto;

    public LoansController( ILoansService loansService ) {
        this.loansService = loansService;
    }

    @Operation(
        summary = "Create Loan REST API",
        description = "REST API to create loan inside bank"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Loan created successfully"
    )
    @PostMapping( "/create" )
    public ResponseEntity<ResponseDto> createLoan( @RequestParam
                                                   @Pattern(regexp="(^$|[0-9]{9})",message = "Mobile number must be 10 digits")
                                                   String mobileNumber ) {
        loansService.createLoan( mobileNumber );
        return ResponseEntity
            .status( HttpStatus.CREATED )
            .body(
                new ResponseDto( HttpStatus.CREATED, LoansConstants.MESSAGE_201 )
            );
    }

    @Operation(
        summary = "Fetch Loan DetailsREST API",
        description = "REST API to fetch loan details based on a mobile number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Loan details fetched successfully"
    )
    @GetMapping( "/fetch" )
    public ResponseEntity<LoanDto> fetchLoanDetails( @RequestParam
                                                     @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
                                                     String mobileNumber ){
        return ResponseEntity
            .status( HttpStatus.OK )
            .body( loansService.fetchLoan( mobileNumber ) );
    }

    @Operation(
        summary = "Update Loan Details REST API",
        description = "REST API to update loan details based on a loan"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Loan updated successfully"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Update failed"
        )
    })
    @PutMapping( "/update" )
    public ResponseEntity<ResponseDto> updateLoanDetails( @Valid @RequestBody LoanDto loanDto ){
        boolean updated = loansService.updateLoan( loanDto );
        if( updated ) {
            return ResponseEntity
                .status( HttpStatus.OK )
                .body( new ResponseDto( HttpStatus.OK, LoansConstants.MESSAGE_200 ) );
        }
        return ResponseEntity
            .status( HttpStatus.EXPECTATION_FAILED )
            .body( new ResponseDto( HttpStatus.EXPECTATION_FAILED, LoansConstants.MESSAGE_417_UPDATE ) );
    }

    @Operation(
        summary = "Delete Loan Details REST API",
        description = "REST API to delete loan details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Loan deleted successfully"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Delete failed"
        )
    })
    @DeleteMapping( "/delete" )
    public ResponseEntity<ResponseDto> deleteLoanDetails( @RequestParam
                                                          @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
                                                          String mobileNumber ){
        boolean deleted = loansService.deleteLoan( mobileNumber );
        if ( deleted ) {
            return ResponseEntity
                .status( HttpStatus.OK )
                .body( new ResponseDto( HttpStatus.OK, LoansConstants.MESSAGE_200 ) );
        }
        return ResponseEntity
            .status( HttpStatus.EXPECTATION_FAILED )
            .body( new ResponseDto( HttpStatus.EXPECTATION_FAILED, LoansConstants.MESSAGE_417_DELETE ) );
    }

    @Operation(
        summary = "Get build information",
        description = "Get build information for the loans microservice"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Build information successfully retrieved"
    )
    @GetMapping( "/build-info" )
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity.ok( buildVersion );
    }

    @Operation(
        summary = "Get Java version",
        description = "Get Java version for the loans microservice"
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
        description = "Get contact info for the loans microservice"
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
