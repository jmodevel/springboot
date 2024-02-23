package com.jmo.devel.tv.shows.exception;

public class ShowNotFoundException extends RuntimeException{

    public ShowNotFoundException( String name ){
        super( String.format( "show not found: %s", name ) );
    }

}
