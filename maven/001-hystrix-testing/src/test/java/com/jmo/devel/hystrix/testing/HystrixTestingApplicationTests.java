package com.jmo.devel.hystrix.testing;

import com.jmo.devel.hystrix.testing.service.Outbound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class HystrixTestingApplicationTests {

	@MockBean
	private Outbound outbound;

	@Autowired
	private HystrixTestingApplication app;

	@Test
	void contextLoads() {
		Assertions.assertNotNull( app );
	}

}
