package com.jmo.devel.customers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomersApplicationTests {

	@Autowired
	private CustomersApplication application;

	@Test
	void contextLoads() {
		Assertions.assertThat( application ).isNotNull();
	}

}
