package com.jmo.devel.tv.shows.exception.handler;

import com.jmo.devel.tv.shows.exception.NoResultsException;
import com.jmo.devel.tv.shows.exception.ShowNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler( ShowNotFoundException.class )
    public ResponseEntity<Object> handleShowNotFoundException( ShowNotFoundException e ) {
        return ResponseEntity
                .status( HttpStatus.NOT_FOUND )
                .body( e.getMessage() );
    }

    @ExceptionHandler( NoResultsException.class )
    public ResponseEntity<Object> handleNoResultsException( NoResultsException e ) {
        return ResponseEntity
                .status( HttpStatus.NOT_FOUND )
                .body( e.getMessage() );
    }

    @ExceptionHandler( RuntimeException.class )
    public ResponseEntity<Object> handleRuntimeException( RuntimeException e ) {
        return ResponseEntity
                .status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( e.getMessage() );
    }

    @ExceptionHandler( NoResourceFoundException.class )
    public ResponseEntity<Object> handleNotFoundError( NoResourceFoundException e ) {
        return ResponseEntity
                .status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( e.getMessage() );
    }

}