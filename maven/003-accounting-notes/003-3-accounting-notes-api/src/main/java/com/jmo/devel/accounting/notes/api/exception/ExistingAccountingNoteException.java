package com.jmo.devel.accounting.notes.api.exception;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ExistingAccountingNoteException extends RuntimeException {

    public ExistingAccountingNoteException( String subject, Instant instant ){
        super(
            String.format( "accounting note with subject %s in %s already exists",
                subject, DateTimeFormatter.ofPattern( "dd/MM/yyyy" ).format( instant.atOffset( ZoneOffset.UTC ).toLocalDate() )
            )
        );
    }

}
