package com.jmo.devel.bookstore.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BookstoreApiApplicationTests {

	@Autowired
	BookstoreApiApplication bookstoreApiApplication;

	@Test
	void contextLoads() {
		assertNotNull( bookstoreApiApplication );
	}

}
