package com.jmo.devel.accounting.notes.api.controller;

import com.jmo.devel.accounting.notes.api.dto.AccountingNoteDto;
import com.jmo.devel.accounting.notes.api.dto.mapper.AccountingNotesModelMapper;
import com.jmo.devel.accounting.notes.api.service.AccountingNotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( value="accounting-notes" )
public class AccountingNotesController {

    private final AccountingNotesService     service;
    private final AccountingNotesModelMapper modelMapper;

    public AccountingNotesController(
        AccountingNotesService     service,
        AccountingNotesModelMapper modelMapper
    ){
        this.service     = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<AccountingNoteDto> create(
        @RequestBody AccountingNoteDto accountingNote
    ) {
        return ResponseEntity.ok(
            this.modelMapper.toDto(
                this.service.create( this.modelMapper.fromDto( accountingNote ) )
            )
        );
    }

    @GetMapping( { "", "/" } )
    public ResponseEntity<List<AccountingNoteDto>> getAll(){
        return ResponseEntity.ok(
            this.service.getAll().stream().map( modelMapper::toDto ).toList()
        );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<AccountingNoteDto> getById( @PathVariable( "id" ) String id ){
        return ResponseEntity.ok(
            this.modelMapper.toDto(
                this.service.getById( id )
            )
        );
    }

    @PutMapping( "/{id}" )
    public ResponseEntity<AccountingNoteDto> update(
        @PathVariable("id") String id,
        @RequestBody AccountingNoteDto accountingNote
    ){
        return ResponseEntity.ok(
            this.modelMapper.toDto(
                this.service.update( id, this.modelMapper.fromDto( accountingNote ) )
            )
        );
    }

    @DeleteMapping( "/{id}" )
    public ResponseEntity<Void> delete( @PathVariable("id") String id ){
        this.service.delete( id );
        return ResponseEntity.noContent().build();
    }

}
