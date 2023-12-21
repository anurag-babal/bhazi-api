package com.example.bhazi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bhazi.profile.ProfileController;

@SpringBootTest
class VeggiesApplicationTests {

	@Autowired
	ProfileController profileController;
	
	@Test
	void contextLoads() {
		assertThat(profileController).isNotNull();
	}

}
