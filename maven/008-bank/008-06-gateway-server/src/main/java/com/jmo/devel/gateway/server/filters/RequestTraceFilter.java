package com.jmo.devel.gateway.server.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Generates a trace ID or correlation ID whenever a new request came to Gateway
 * from external client applications
 */
@Order(1) // filters execution order
@Component
public class RequestTraceFilter implements GlobalFilter { // all kinds of traffic

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    // Mono: single object returning nothing, Flux: multiple objects
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if( filterUtility.getCorrelationId( requestHeaders ) != null ) {
            logger.debug( "correlation-id found in RequestTraceFilter : {}",
                filterUtility.getCorrelationId( requestHeaders ) );
        } else {
            String correlationID = java.util.UUID.randomUUID().toString();
            exchange = filterUtility.setCorrelationId( exchange, correlationID );
            logger.debug( "correlation-id generated in RequestTraceFilter : {}", correlationID );
        }
        return chain.filter( exchange );
    }

}