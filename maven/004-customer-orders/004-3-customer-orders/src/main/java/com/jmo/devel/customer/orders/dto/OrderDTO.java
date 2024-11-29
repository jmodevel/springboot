package com.jmo.devel.customer.orders.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Integer id;
    private Integer customerId;
    private String  status;
    private Double  totalAmount;

}
