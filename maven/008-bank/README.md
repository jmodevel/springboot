# Gateway Server

## Routes
- http://localhost:8072/actuator/gateway/routes: predicates, filters and uris: forward the request to an instance of MS in uri
- f.rewritePath("/loans/(?<remaining>.*)", "/${remaining}): when request is forwarded, it removes the prefix value which is received by loans MS
- uri: lb:/LOANS: load balancer for application LOANS in Eureka Server: the request is forwarded to this MS

## Headers in methods
- AccountsController: fetchCustomerDetails: @RequestParam("correlation-id") String correlationId, header available in method

# Resilience4J
- http://localhost:8072/actuator/circuitbreakers: status of circuit breakers: -1.0% means no requests failed
- http://localhost:8072/actuator/circuitbreakerevents: events of circuit breakers

# Redis for Rate Limiter Pattern
- Start Redis container: docker run -p 6379:6379 --name redis -d redis
- Setup properties:
  - spring.data.redis.connect-timeout=2s
  - spring.data.redis.host=localhost
  - spring.data.redis.port=6379
  - spring.data.redis.timeout=1s

# Apache Bench to send multiple requests within a second
- ab -n 10 -c 2 -v 3 http://localhost:8072/bank/cards/api/contact-info
- -n 10: number of requests
- -c 2: concurrency: two requests each time
- -v 3: verbose level: detailed report in the output