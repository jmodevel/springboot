package com.jmo.devel.accounts.mapper;

import com.jmo.devel.accounts.dto.CustomerDetailsDto;
import com.jmo.devel.accounts.dto.CustomerDto;
import com.jmo.devel.accounts.model.Customer;

public class CustomersMapper {

    public static CustomerDto toCustomerDto(Customer customer ) {
        return CustomerDto.builder()
            .name        ( customer.getName()         )
            .email       ( customer.getEmail()        )
            .mobileNumber( customer.getMobileNumber() )
            .build();
    }

    public static CustomerDetailsDto toCustomerDetailsDto( Customer customer ) {
        return CustomerDetailsDto.builder()
            .name        ( customer.getName()         )
            .email       ( customer.getEmail()        )
            .mobileNumber( customer.getMobileNumber() )
            .build();
    }

    public static Customer toCustomer( CustomerDto customerDto ) {
        return Customer.builder()
            .name        ( customerDto.getName()         )
            .email       ( customerDto.getEmail()        )
            .mobileNumber( customerDto.getMobileNumber() )
            .build();
    }

    public static Customer updateCustomer( Customer customer, CustomerDto customerDto ) {
        customer.setName        ( customerDto.getName()         );
        customer.setEmail       ( customerDto.getEmail()        );
        customer.setMobileNumber( customerDto.getMobileNumber() );
        return customer;
    }

}
