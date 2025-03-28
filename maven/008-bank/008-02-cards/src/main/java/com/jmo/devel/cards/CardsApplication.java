package com.jmo.devel.cards;

import com.jmo.devel.cards.dto.ContactInfoDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing( auditorAwareRef = "auditAware" )
@EnableConfigurationProperties( value = {ContactInfoDto.class} )
@OpenAPIDefinition(
	info=@Info(
		title = "Cards Microservice REST API Documentation",
		description = "Cards Microservice REST API Documentation",
		version = "1.0.0",
		contact = @Contact(
			name = "JMO Devel",
			email = "jmo.devel@gmail.com"
		)
	)
)
@Tag(
	name = "CRUD REST API for Cards Microservice",
	description = "CRUD REST API for Cards Microservice REST API Details"
)
public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
