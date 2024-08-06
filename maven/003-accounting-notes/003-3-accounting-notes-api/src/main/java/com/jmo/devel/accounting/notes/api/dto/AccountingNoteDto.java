package com.jmo.devel.accounting.notes.api.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountingNoteDto {

    private String id;
    private String subject;
    private String category;
    private double amount;
    private LocalDate date;

}
