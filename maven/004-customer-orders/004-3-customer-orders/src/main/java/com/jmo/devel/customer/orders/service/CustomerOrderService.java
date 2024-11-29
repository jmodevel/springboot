package com.jmo.devel.customer.orders.service;

import com.jmo.devel.customer.orders.dto.CustomerOrderDTO;

public interface CustomerOrderService {

    CustomerOrderDTO getCustomerOrder( Integer id ) throws Exception;

}
