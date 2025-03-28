package com.jmo.devel.cards.exception;

import com.jmo.devel.cards.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(
            ex.getBindingResult().getAllErrors().stream()
                .collect( Collectors.toMap( error -> ((FieldError) error).getField(), ObjectError::getDefaultMessage) ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<ErrorDto> handleGlobalException(
        Exception exception, WebRequest webRequest
    ) {
        return ResponseEntity
            .status( HttpStatus.INTERNAL_SERVER_ERROR )
            .body(
                new ErrorDto(
                    webRequest.getDescription( false ),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    LocalDateTime.now()
                )
            );
    }

    @ExceptionHandler( CardAlreadyExistsException.class )
    public ResponseEntity<ErrorDto> handleCardAlreadyExistsException(
        CardAlreadyExistsException exception, WebRequest webRequest
    ) {
        return ResponseEntity
            .status( HttpStatus.BAD_REQUEST )
            .body(
                new ErrorDto(
                    webRequest.getDescription( false ),
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    LocalDateTime.now()
                )
            );
    }

    @ExceptionHandler( ResourceNotFoundException.class )
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(
        ResourceNotFoundException exception, WebRequest webRequest
    ) {
        return ResponseEntity
            .status( HttpStatus.NOT_FOUND )
            .body(
                new ErrorDto(
                    webRequest.getDescription( false ),
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    LocalDateTime.now()
                )
            );
    }

}
