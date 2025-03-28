# Gateway Server

## Routes
- http://localhost:8072/actuator/gateway/routes: predicates, filters and uris: forward the request to an instance of MS in uri
- f.rewritePath("/loans/(?<remaining>.*)", "/${remaining}): when request is forwarded, it removes the prefix value which is received by loans MS
- uri: lb:/LOANS: load balancer for application LOANS in Eureka Server: the request is forwarded to this MS

## Headers in methods
- AccountsController: fetchCustomerDetails: @RequestParam("correlation-id") String correlationId, header available in method