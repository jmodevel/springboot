package com.jmo.devel.accounting.notes.api.dto.mapper;

import com.jmo.devel.accounting.notes.api.dto.AccountingNoteDto;
import com.jmo.devel.accounting.notes.api.model.AccountingNote;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountingNotesModelMapper {

    private final ModelMapper modelMapper;

    public AccountingNotesModelMapper(ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    public AccountingNoteDto toDto( AccountingNote accountingNote ){
        return this.modelMapper.map( accountingNote, AccountingNoteDto.class );
    }

    public AccountingNote fromDto( AccountingNoteDto accountingNote ){
        return this.modelMapper.map( accountingNote, AccountingNote.class );
    }

}
