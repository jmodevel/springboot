package com.jmo.devel.accounting.notes.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

	@Autowired
	AccountingNotesApplication accountingNotesApplication;

	@Test
	void contextLoads() {
		assertNotNull( accountingNotesApplication );
	}

}
