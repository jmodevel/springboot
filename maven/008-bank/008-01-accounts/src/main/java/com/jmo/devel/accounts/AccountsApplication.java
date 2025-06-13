package com.jmo.devel.accounts;

import com.jmo.devel.accounts.dto.ContactInfoDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing( auditorAwareRef = "auditAware" )
@EnableConfigurationProperties( value = {ContactInfoDto.class} )
@OpenAPIDefinition(
	info=@Info(
		title = "Accounts Microservice REST API Documentation",
		description = "Accounts Microservice REST API Documentation",
		version = "1.0.0",
		contact = @Contact(
			name = "JMO Devel",
			email = "jmo.devel@gmail.com"
		)
	)
)
@Tag(
	name = "CRUD REST API for Accounts Microservice",
	description = "CRUD REST API for Accounts Microservice REST API Details"
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
