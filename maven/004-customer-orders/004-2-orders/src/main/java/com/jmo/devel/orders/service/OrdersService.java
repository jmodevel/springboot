package com.jmo.devel.orders.service;

import com.jmo.devel.orders.dto.OrderDTO;
import com.jmo.devel.orders.model.Order;

public interface OrdersService {

    Order getOrder(Integer id ) throws Exception;

}
