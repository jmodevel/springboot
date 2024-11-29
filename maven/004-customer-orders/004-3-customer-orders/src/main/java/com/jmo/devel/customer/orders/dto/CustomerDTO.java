package com.jmo.devel.customer.orders.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Integer id;
    private String  name;
    private String  surname;
    private String  city;
    private String  country;

}
