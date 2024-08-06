package com.jmo.devel.accounting.notes.api;

import com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration;
import com.google.cloud.spring.data.firestore.repository.config.EnableReactiveFirestoreRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude={GcpContextAutoConfiguration.class})
@EnableReactiveFirestoreRepositories
public class AccountingNotesApplication {

	public static void main(String[] args) {
		SpringApplication.run( AccountingNotesApplication.class, args );
	}

}
