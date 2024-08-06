package com.jmo.devel.accounting.notes.api.service;

import com.jmo.devel.accounting.notes.api.exception.AccountingNoteNotFoundException;
import com.jmo.devel.accounting.notes.api.exception.ExistingAccountingNoteException;
import com.jmo.devel.accounting.notes.api.exception.NoResultsException;
import com.jmo.devel.accounting.notes.api.model.AccountingNote;
import com.jmo.devel.accounting.notes.api.repository.AccountingNotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AccountingNotesService {

    private final AccountingNotesRepository repository;

    public AccountingNotesService( AccountingNotesRepository repository ){
        this.repository = repository;
    }

    public AccountingNote create( AccountingNote accountingNote ){
        Optional<AccountingNote> existing =
            repository.findBySubjectAndDate(
                accountingNote.getSubject(), accountingNote.getDate()
            ).blockOptional();
        if( existing.isPresent() ){
            throw new ExistingAccountingNoteException(
                accountingNote.getSubject(), accountingNote.getDate()
            );
        }
        return repository.save( accountingNote ).block();
    }

    public List<AccountingNote> getAll(){
        Flux<AccountingNote> categories = this.repository.findAll();
        if( Objects.requireNonNull( categories.collectList().block() ).isEmpty() ) {
            throw new NoResultsException( "categories", "all");
        }
        return categories.collectList().block();
    }

    public AccountingNote getById( String id ){
        return this.repository.findById( id ).blockOptional()
            .orElseThrow( () -> new AccountingNoteNotFoundException( "id", id ) );
    }

    public AccountingNote getBySubjectAndDate( String subject, Instant date ){
        return this.repository.findBySubjectAndDate( subject, date )
            .blockOptional()
            .orElseThrow( () -> new AccountingNoteNotFoundException(
                "subject and date",
                subject + " in " + date.toString() )
            );
    }

    public AccountingNote update( String id, AccountingNote accountingNote ){
        Optional<AccountingNote> existing = repository.findById( id ).blockOptional();
        if( existing.isEmpty() ){
            throw new AccountingNoteNotFoundException( "id", id );
        }
        accountingNote.setId( id );
        return repository.save( accountingNote ).block();
    }

    public void delete( String id ){
        Optional<AccountingNote> existing = repository.findById( id ).blockOptional();
        if( existing.isEmpty() ){
            throw new AccountingNoteNotFoundException( "id", id );
        }
        repository.deleteById( id ).subscribe(
            null,
            error -> log.error( "Failed to delete entity: " + error.getMessage() ),
            ()    -> log.debug( "Entity deleted successfully." )
        );
    }

}
