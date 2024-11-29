package com.jmo.devel.orders.dto.mapper;

import com.jmo.devel.orders.dto.OrderDTO;
import com.jmo.devel.orders.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrdersModelMapper {

    private final ModelMapper modelMapper;

    public OrdersModelMapper( ModelMapper modelMapper ){
        this.modelMapper = modelMapper;
    }

    public OrderDTO toDto( Order order ){
        return this.modelMapper.map( order, OrderDTO.class );
    }

    public Order fromDto( OrderDTO orderDto ){
        return this.modelMapper.map( orderDto, Order.class );
    }

}
