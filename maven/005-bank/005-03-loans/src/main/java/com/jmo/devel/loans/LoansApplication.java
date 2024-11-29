package com.jmo.devel.loans;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing( auditorAwareRef = "auditAware" )
@OpenAPIDefinition(
	info=@Info(
		title = "Loans Microservice REST API Documentation",
		description = "Loans Microservice REST API Documentation",
		version = "1.0.0",
		contact = @Contact(
			name = "JMO Devel",
			email = "jmo.devel@gmail.com"
		)
	)
)
@Tag(
	name = "CRUD REST API for Loans Microservice",
	description = "CRUD REST API for Loans Microservice REST API Details"
)
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

}
