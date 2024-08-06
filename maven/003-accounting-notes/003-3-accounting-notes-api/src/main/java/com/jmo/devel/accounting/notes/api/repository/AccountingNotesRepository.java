package com.jmo.devel.accounting.notes.api.repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.jmo.devel.accounting.notes.api.model.AccountingNote;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Repository
public interface AccountingNotesRepository extends FirestoreReactiveRepository<AccountingNote> {
    Mono<AccountingNote> findBySubjectAndDate( String subject, Instant date );

}
