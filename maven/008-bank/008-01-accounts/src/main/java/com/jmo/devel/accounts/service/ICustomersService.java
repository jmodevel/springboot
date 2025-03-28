package com.jmo.devel.accounts.service;

import com.jmo.devel.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {

    CustomerDetailsDto fetchCustomerDetails( String mobileNumber, String correlationId );

}
