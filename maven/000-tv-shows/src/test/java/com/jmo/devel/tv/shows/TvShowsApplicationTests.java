package com.jmo.devel.tv.shows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TvShowsApplicationTests {

	@Autowired
	private TvShowsApplication app;

	@Test
	void contextLoads() {
		Assertions.assertNotNull( app );
	}

}
