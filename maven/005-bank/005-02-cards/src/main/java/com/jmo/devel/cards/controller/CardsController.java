package com.jmo.devel.cards.controller;

import com.jmo.devel.cards.constants.CardsConstants;
import com.jmo.devel.cards.dto.CardDto;
import com.jmo.devel.cards.dto.ContactInfoDto;
import com.jmo.devel.cards.dto.ResponseDto;
import com.jmo.devel.cards.service.ICardsService;
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
public class CardsController {

    private final ICardsService cardsService;

    @Value( "${build.version}" )
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private ContactInfoDto contactInfoDto;

    public CardsController( ICardsService cardsService ) {
        this.cardsService = cardsService;
    }

    @Operation(
        summary = "Create Card REST API",
        description = "REST API to create card inside bank"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Card created successfully"
    )
    @PostMapping( "/create" )
    public ResponseEntity<ResponseDto> createCard( @RequestParam
                                                   @Pattern(regexp="(^$|[0-9]{9})",message = "Mobile number must be 10 digits")
                                                   String mobileNumber ) {
        cardsService.createCard( mobileNumber );
        return ResponseEntity
            .status( HttpStatus.CREATED )
            .body(
                new ResponseDto( HttpStatus.CREATED, CardsConstants.MESSAGE_201 )
            );
    }

    @Operation(
        summary = "Fetch Card DetailsREST API",
        description = "REST API to fetch card details based on a mobile number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Card details fetched successfully"
    )
    @GetMapping( "/fetch" )
    public ResponseEntity<CardDto> fetchCardDetails( @RequestParam
                                                     @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
                                                     String mobileNumber ){
        return ResponseEntity
            .status( HttpStatus.OK )
            .body( cardsService.fetchCard( mobileNumber ) );
    }

    @Operation(
        summary = "Update Card Details REST API",
        description = "REST API to update card details based on a card"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Card updated successfully"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Update failed"
        )
    })
    @PutMapping( "/update" )
    public ResponseEntity<ResponseDto> updateCardDetails( @Valid @RequestBody CardDto cardDto ){
        boolean updated = cardsService.updateCard( cardDto );
        if( updated ) {
            return ResponseEntity
                .status( HttpStatus.OK )
                .body( new ResponseDto( HttpStatus.OK, CardsConstants.MESSAGE_200 ) );
        }
        return ResponseEntity
            .status( HttpStatus.EXPECTATION_FAILED )
            .body( new ResponseDto( HttpStatus.EXPECTATION_FAILED, CardsConstants.MESSAGE_417_UPDATE ) );
    }

    @Operation(
        summary = "Delete Card Details REST API",
        description = "REST API to delete card details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Card deleted successfully"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Delete failed"
        )
    })
    @DeleteMapping( "/delete" )
    public ResponseEntity<ResponseDto> deleteCardDetails( @RequestParam
                                                          @Pattern( regexp = "^[0-9]{9}$", message = "mobile number is not valid" )
                                                          String mobileNumber ){
        boolean deleted = cardsService.deleteCard( mobileNumber );
        if ( deleted ) {
            return ResponseEntity
                .status( HttpStatus.OK )
                .body( new ResponseDto( HttpStatus.OK, CardsConstants.MESSAGE_200 ) );
        }
        return ResponseEntity
            .status( HttpStatus.EXPECTATION_FAILED )
            .body( new ResponseDto( HttpStatus.EXPECTATION_FAILED, CardsConstants.MESSAGE_417_DELETE ) );
    }

    @Operation(
        summary = "Get build information",
        description = "Get build information for the cards microservice"
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
        description = "Get contact info for the cards microservice"
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
