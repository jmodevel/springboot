package com.jmo.devel.bookstore.api.exception.handler;

import com.jmo.devel.bookstore.api.exception.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler( {
        BookNotFoundException.class,
        AuthorNotFoundException.class,
        PublisherNotFoundException.class,
        NoResultsException.class
    } )
    public ResponseEntity<Object> handleNotFoundException( Exception e ) {
        return ResponseEntity
            .status( HttpStatus.NOT_FOUND )
            .body( EntityModel.of( e ) );
    }

    @ExceptionHandler( {
        ExistingBookException.class,
        ExistingAuthorException.class,
        ExistingPublisherException.class
    } )
    public ResponseEntity<Object> handleExistingElementException( Exception e ) {
        return ResponseEntity
            .status( HttpStatus.CONFLICT )
            .body( EntityModel.of( e ) );
    }

    @ExceptionHandler( RuntimeException.class )
    public ResponseEntity<Object> handleRuntimeException( RuntimeException e ) {
        return ResponseEntity
            .internalServerError()
            .body( EntityModel.of( e ) );
    }

    @ExceptionHandler( NoResourceFoundException.class )
    public ResponseEntity<Object> handleNotFoundError( NoResourceFoundException e ) {
        return ResponseEntity
            .internalServerError()
            .body( EntityModel.of( e ) );
    }

}
