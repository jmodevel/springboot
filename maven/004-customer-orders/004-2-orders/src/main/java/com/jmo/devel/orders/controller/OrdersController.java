package com.jmo.devel.orders.controller;

import com.jmo.devel.orders.dto.OrderDTO;
import com.jmo.devel.orders.dto.mapper.OrdersModelMapper;
import com.jmo.devel.orders.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "orders" )
public class OrdersController {

    private final OrdersService     service;
    private final OrdersModelMapper modelMapper;

    public OrdersController( OrdersService service, OrdersModelMapper modelMapper ){
        this.service     = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping( value = "/{id}" )
    public OrderDTO getOrder( @PathVariable Integer id ) throws Exception {
        return this.modelMapper.toDto( service.getOrder( id ) );
    }

}
