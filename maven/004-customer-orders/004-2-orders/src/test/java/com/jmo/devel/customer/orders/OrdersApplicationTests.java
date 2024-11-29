package com.jmo.devel.customer.orders;

import com.jmo.devel.orders.OrdersApplication;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrdersApplicationTests {

	@Autowired
	private OrdersApplication application;

	@Test
	void contextLoads() {
		Assertions.assertThat( application ).isNotNull();
	}

}
