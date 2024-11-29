package com.jmo.devel.customers.controller;

import com.jmo.devel.customers.dto.CustomerDTO;
import com.jmo.devel.customers.dto.mapper.CustomersModelMapper;
import com.jmo.devel.customers.service.CustomersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "customers" )
public class CustomersController {

    private final CustomersService     service;
    private final CustomersModelMapper modelMapper;

    public CustomersController( CustomersService service, CustomersModelMapper modelMapper ){
        this.service     = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping( value = "/{id}" )
    public CustomerDTO getCustomer( @PathVariable Integer id ) throws Exception {
        return this.modelMapper.toDto( service.getCustomer( id ) );
    }

}
