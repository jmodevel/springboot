package com.jmo.devel.accounting.notes.api.integration;

import com.jmo.devel.accounting.notes.api.dto.AccountingNoteDto;
import com.jmo.devel.accounting.notes.api.model.AccountingNote;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith( SpringExtension.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class AccountingNotesApiTest {

    public static final String ACCOUNTING_NOTES_PATH = "/accounting-notes";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void givenAccountingNote_whenItIsCreatedReadUpdatedAndDeleted_thenItIsDeleted() {

        final Response getAllResponse = get( ACCOUNTING_NOTES_PATH );
        assertEquals( HttpStatus.OK.value(), getAllResponse.getStatusCode() );

        int accountingNotesSize = getAllResponse.as( List.class ).size();

        AccountingNoteDto sample = AccountingNoteDto.builder()
            .subject( "Amazon" )
            .category( "Comercio" )
            .amount( 19.83 )
            .date( LocalDate.now() )
            .build();

        Response createResponse = given()
            .contentType( ContentType.JSON )
            .body( sample )
            .post( ACCOUNTING_NOTES_PATH );
        assertEquals( HttpStatus.OK.value(), createResponse.getStatusCode() );
        String sampleId = createResponse.body().jsonPath().getString( "id" );
        assertNotNull( sampleId );
        assertEquals( "Amazon",   createResponse.body().jsonPath().getString( "subject" ) );
        assertEquals( "Comercio", createResponse.body().jsonPath().getString( "category" ) );
        assertEquals( 19.83,      createResponse.body().jsonPath().getDouble( "amount" ) );
        assertEquals(
            LocalDate.now().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) ),
            createResponse.body().jsonPath().get( "date" )
        );

        assertEquals( accountingNotesSize+1, get( ACCOUNTING_NOTES_PATH ).as( List.class ).size() );

        Response getByIdResponse = get(ACCOUNTING_NOTES_PATH + "/" + sampleId );
        assertEquals( HttpStatus.OK.value(), getByIdResponse.getStatusCode() );
        assertEquals( sampleId, getByIdResponse.body().jsonPath().getString( "id" ) );

        sample.setId( sampleId );
        sample.setAmount( 20.94 );
        Response updateResponse = given()
            .contentType( MediaType.APPLICATION_JSON_VALUE )
            .body( sample )
            .put( ACCOUNTING_NOTES_PATH + "/" + sampleId );
        assertEquals( HttpStatus.OK.value(), updateResponse.getStatusCode() );
        assertEquals( sampleId, updateResponse.body().jsonPath().getString( "id" ) );
        assertEquals( "Amazon",   updateResponse.body().jsonPath().getString( "subject" ) );
        assertEquals( 20.94, updateResponse.body().jsonPath().getDouble( "amount" ) );

        Response deleteResponse = delete( ACCOUNTING_NOTES_PATH + "/" + sampleId );
        assertEquals( HttpStatus.NO_CONTENT.value(), deleteResponse.getStatusCode());

        Response getAfterDeletionResponse = get(ACCOUNTING_NOTES_PATH + "/" + sampleId );
        assertNotEquals( HttpStatus.NOT_FOUND.value(), getAfterDeletionResponse.getStatusCode() );

    }

}