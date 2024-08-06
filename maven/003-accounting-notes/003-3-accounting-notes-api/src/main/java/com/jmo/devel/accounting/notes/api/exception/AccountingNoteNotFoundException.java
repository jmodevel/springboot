package com.jmo.devel.accounting.notes.api.exception;

public class AccountingNoteNotFoundException extends RuntimeException {

    public AccountingNoteNotFoundException( String type, String value ){
        super( String.format( "accounting note not found by %s with %s", type, value )  );
    }

}
