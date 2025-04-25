package com.jmo.devel.gateway.server;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
					.circuitBreaker( config -> config.setName( "accountsCircuitBreaker" )
						.setFallbackUri( "forward:/contactSupport" )
					)
				)
				.uri( "lb://ACCOUNTS" )) // Eureka name
			.route( p -> p
				.path( "/bank/loans/**" )
				.filters( f -> f.rewritePath( "/bank/loans/(?<segment>.*)", "/${segment}" )
					.addResponseHeader( "X-Response-Time", LocalDateTime.now().toString() )
					.retry( config ->
						config.setRetries( 3 )
							.setMethods( HttpMethod.GET )
							// wait for 100ms to initiate a retry
							// maximum interval between two retries is 1000ms
							.setBackoff( Duration.ofMillis(100), Duration.ofMillis(1000), 2, true )
					)
				)
				.uri( "lb://LOANS" )) // Eureka name
			.route( p -> p
				.path( "/bank/cards/**" )
				.filters( f -> f.rewritePath( "/bank/cards/(?<segment>.*)", "/${segment}" )
					.addResponseHeader( "X-Response-Time", LocalDateTime.now().toString() )
					.requestRateLimiter( config ->
						config.setRateLimiter( redisRateLimiter() )
							.setKeyResolver( userKeyResolver() )
					)
				)
				.uri( "lb://CARDS" )) // Eureka name
			.build();
	}

	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault( id -> new Resilience4JConfigBuilder( id )
			.circuitBreakerConfig( CircuitBreakerConfig.ofDefaults() )
			.timeLimiterConfig( TimeLimiterConfig.custom().timeoutDuration( Duration.ofSeconds( 4 ) ).build() )
			.build()
		);
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		// for each second, end user can only make one request
		return new RedisRateLimiter( 1, 1, 1 );
	}

	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(
			exchange.getRequest().getHeaders().getFirst( "user" )
		).defaultIfEmpty( "anonymous" );
	}



}
