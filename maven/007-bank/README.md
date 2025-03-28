# Config

## Server
- Config file eureka-server.yml in Github Repo must have the same name as the one in spring.application.name
- eureka.client.fetch-registry: false > Eureka server don`t fetch the registry details of other microservices 
- eureka.client.register-with-eureka: false > Eureka server don`t register with Eureka and don't expose its details
- http://localhost:8071/eureka-server/default > Access configuration for Eureka Server
- http://localhost:8070/eureka/apps > All the apps and the instances that are registered with the Eureka Server
- http://localhost:8070/ > Eureka Dashboard: details of other MS

## Client
- eureka.instance.prefer-ip-address: true > Client prefer IP address (no DNS, if not, it will try to register localhost name)
- http://localhost:8080/actuator/shutdown > Shutdown gracefully by Actuator

## Feign
- @FeignClient("cards") > name of target MS in EurekaServer
- Load Balancing:
  - Create 2 instances of loans MS by starting 2 docker containers of this microservices
  - Create card, account and loan (only POST to one port)
  - Request customer details (which involves feign client calls) 
  - The response will be successful or INTERNAL_SERVER_ERROR (404): cannot fund the given input data mobileNumber