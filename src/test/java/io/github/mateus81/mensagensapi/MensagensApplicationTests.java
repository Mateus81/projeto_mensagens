package io.github.mateus81.mensagensapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import io.github.mateus81.mensagensapi.config.TestConfig;
import io.github.mateus81.mensagensapi.model.service.UserDetailsServiceImpl;

@SpringBootTest
@ContextConfiguration(classes = {MensagensApplication.class, TestConfig.class})
class MensagensApplicationTests {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Test
	void contextLoads() {
		var user = userDetailsService.loadUserByUsername("testUser");
		System.out.println("Usuario carregado: " + user.getUsername());
	}

}
