package com.jmo.devel.customer.orders.controller;

import com.jmo.devel.customer.orders.dto.CustomerOrderDTO;
import com.jmo.devel.customer.orders.service.CustomerOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "customer-orders" )
public class CustomerOrderController {

    private final CustomerOrderService service;

    public CustomerOrderController( CustomerOrderService service ){
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public CustomerOrderDTO getCustomerOrder( @PathVariable Integer id ) throws Exception {
        return service.getCustomerOrder( id );
    }

}
