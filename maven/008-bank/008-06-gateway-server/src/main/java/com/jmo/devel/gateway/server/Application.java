package com.jmo.devel.gateway.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RouteLocator bankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
			.route( p -> p
				.path( "/bank/accounts/**" )
				.filters( f -> f.rewritePath( "/bank/accounts/(?<segment>.*)", "/${segment}" )
					.addResponseHeader( "X-Response-Time", LocalDateTime.now().toString() )
				)
				.uri( "lb://ACCOUNTS" )) // Eureka name
			.route( p -> p
				.path( "/bank/loans/**" )
				.filters( f -> f.rewritePath( "/bank/loans/(?<segment>.*)", "/${segment}" )
					.addResponseHeader( "X-Response-Time", LocalDateTime.now().toString() )
				)
				.uri( "lb://LOANS" )) // Eureka name
			.route( p -> p
				.path( "/bank/cards/**" )
				.filters( f -> f.rewritePath( "/bank/cards/(?<segment>.*)", "/${segment}" )
					.addResponseHeader( "X-Response-Time", LocalDateTime.now().toString() )
				)
				.uri( "lb://CARDS" )) // Eureka name
			.build();
	}

}
