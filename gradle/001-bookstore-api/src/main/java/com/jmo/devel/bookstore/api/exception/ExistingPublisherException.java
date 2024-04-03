package com.jmo.devel.bookstore.api.exception;

public class ExistingPublisherException extends RuntimeException {

    public ExistingPublisherException( String name ){
        super( String.format( "publisher with name %s already exists", name ) );
    }

}
