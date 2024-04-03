package com.jmo.devel.bookstore.api.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException( String type, String value ){
        super( String.format( "book not found by %s with %s", type, value )  );
    }

}
