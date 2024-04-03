package com.jmo.devel.bookstore.api.exception;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException( String type, String value ){
        super( String.format( "author not found by %s with %s", type, value )  );
    }

}
