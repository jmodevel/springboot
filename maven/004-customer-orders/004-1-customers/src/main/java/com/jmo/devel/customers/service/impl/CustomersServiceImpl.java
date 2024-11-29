package com.jmo.devel.customers.service.impl;

import com.jmo.devel.customers.model.Customer;
import com.jmo.devel.customers.repository.CustomersRepository;
import com.jmo.devel.customers.service.CustomersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomersServiceImpl implements CustomersService {

    private final CustomersRepository repository;

    public CustomersServiceImpl(CustomersRepository repository ){
        this.repository = repository;
    }

    @Override
    public Customer getCustomer( Integer id ) throws Exception {
        log.info( "Getting customer details for {}", id );
        Optional<Customer> customer = this.repository.findById( id );
        if ( customer.isPresent() ) {
            return customer.get();
        } else {
            log.error( "No customer found for {}", id );
            return null;
        }
    }

}