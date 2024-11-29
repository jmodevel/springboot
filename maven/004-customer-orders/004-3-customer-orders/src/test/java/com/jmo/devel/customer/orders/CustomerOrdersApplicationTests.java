package com.jmo.devel.customer.orders;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerOrdersApplicationTests {

	@Autowired
	private CustomerOrdersApplication application;

	@Test
	void contextLoads() {
		Assertions.assertThat( application ).isNotNull();
	}

}
