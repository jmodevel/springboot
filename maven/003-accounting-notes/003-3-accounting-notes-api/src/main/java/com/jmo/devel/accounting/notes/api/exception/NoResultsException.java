package com.jmo.devel.accounting.notes.api.exception;

public class NoResultsException extends RuntimeException {

    public NoResultsException( String type, String value ){
        super( String.format( "no results for %s with %s", type, value )  );
    }

}
