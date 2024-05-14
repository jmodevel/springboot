package com.jmo.devel.bookstore.admin;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookstoreAdminApplicationTests {

	@Autowired
	BookstoreAdminApplication bookstoreAdminApplication;

	@Test
	void contextLoads() {
		Assertions.assertThat( bookstoreAdminApplication ).isNotNull();
	}

}
