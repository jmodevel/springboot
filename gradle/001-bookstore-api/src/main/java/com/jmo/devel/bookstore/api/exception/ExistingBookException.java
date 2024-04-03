package com.jmo.devel.bookstore.api.exception;

public class ExistingBookException extends RuntimeException {

    public ExistingBookException( String isbn ){
        super( String.format( "book with isbn %s already exists", isbn ) );
    }

}
