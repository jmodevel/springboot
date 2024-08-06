package com.jmo.devel.accounting.notes.api.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.*;

import java.time.Instant;

@Document( collectionName = "accountingNotes" )
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountingNote {

	@DocumentId
	private String  id;
	private String  subject;
	private String  category;
	private double  amount;
	private Instant date;
	
}
