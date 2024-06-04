package com.jmo.devel.eureka.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EurekaServerApplicationTests {

	@Autowired
	EurekaServerApplication eurekaServerApplication;

	@Test
	void contextLoads() {
		Assertions.assertNotNull( eurekaServerApplication );
	}

}
