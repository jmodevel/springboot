package com.jmo.devel.customer.orders.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDTO {

    private CustomerDTO customer;
    private OrderDTO    order;

}
