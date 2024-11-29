package com.jmo.devel.customer.orders.service.impl;

import com.jmo.devel.customer.orders.dto.CustomerDTO;
import com.jmo.devel.customer.orders.dto.CustomerOrderDTO;
import com.jmo.devel.customer.orders.dto.OrderDTO;
import com.jmo.devel.customer.orders.service.CustomerOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private final RestTemplate restTemplate;

    public CustomerOrderServiceImpl( RestTemplate restTemplate ){
        this.restTemplate = restTemplate;
    }

    @Value("${customers.service.url}")
    private String customersServiceURL;

    @Value("${orders.service.url}")
    private String ordersServiceURL;

    @Override
    public CustomerOrderDTO getCustomerOrder( Integer id ) throws Exception {
        log.info( "Getting customer and order details for {}", id );
        OrderDTO order = getOrder( id );
        if (order != null) {
            return CustomerOrderDTO.builder()
                .order   ( order )
                .customer( getCustomer( order.getCustomerId() ) )
                .build();
        } else {
            log.error( "No order found for {}", id );
            return CustomerOrderDTO.builder().build();
        }
    }

    private CustomerDTO getCustomer( Integer id ) {
        log.debug( "Getting customer identified by {}", id );
        ResponseEntity<CustomerDTO> customerDTOResponse =
            restTemplate.getForEntity( customersServiceURL + id, CustomerDTO.class );
        log.info( "Result of getting customer {} is {}", id, customerDTOResponse.getStatusCode().value() );
        return customerDTOResponse.getBody();
    }

    private OrderDTO getOrder( Integer id ) {
        log.debug( "Getting order identified by {}", id );
        ResponseEntity<OrderDTO> orderDTOResponse =
            restTemplate.getForEntity( ordersServiceURL + id, OrderDTO.class );
        log.info( "Result of getting order {} is {}", id, orderDTOResponse.getStatusCode().value() );
        return orderDTOResponse.getBody();
    }

}