package com.jmo.devel.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	DemoApplication demoApplication;

	@Test
	void contextLoads() {
		assertNotNull( demoApplication );
	}

}