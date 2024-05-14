package com.jmo.devel.bookstore.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookstoreAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreAdminApplication.class, args);
	}

}
