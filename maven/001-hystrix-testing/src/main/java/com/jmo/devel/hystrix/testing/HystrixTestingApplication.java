package com.jmo.devel.hystrix.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class HystrixTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixTestingApplication.class, args);
	}

}