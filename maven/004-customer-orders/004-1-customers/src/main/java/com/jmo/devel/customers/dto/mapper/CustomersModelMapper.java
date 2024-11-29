package com.jmo.devel.customers.dto.mapper;

import com.jmo.devel.customers.dto.CustomerDTO;
import com.jmo.devel.customers.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomersModelMapper {

    private final ModelMapper modelMapper;

    public CustomersModelMapper( ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    public CustomerDTO toDto( Customer customer){
        return this.modelMapper.map( customer, CustomerDTO.class );
    }

    public Customer fromDto( CustomerDTO customerDto ){
        return this.modelMapper.map( customerDto, Customer.class );
    }

}
