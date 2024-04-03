package com.jmo.devel.bookstore.api.exception;

public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException(String type, String value ){
        super( String.format( "publisher not found by %s with %s", type, value )  );
    }

}
