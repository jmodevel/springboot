package com.jmo.devel.bookstore.api.exception;

public class ExistingAuthorException extends RuntimeException {

    public ExistingAuthorException( String name, String surnames ){
        super( String.format( "author %s %s already exists", name, surnames ) );
    }

}
