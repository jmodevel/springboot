package com.jmo.devel.orders.service.impl;

import com.jmo.devel.orders.model.Order;
import com.jmo.devel.orders.repository.OrdersRepository;
import com.jmo.devel.orders.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository repository;

    public OrdersServiceImpl( OrdersRepository repository ){
        this.repository = repository;
    }

    @Override
    public Order getOrder( Integer id ) throws Exception {
        log.info( "Getting order details for {}", id );
        Optional<Order> order = this.repository.findById( id );
        if ( order.isPresent() ) {
            return order.get();
        } else {
            log.error( "No order found for {}", id );
            return null;
        }
    }

}