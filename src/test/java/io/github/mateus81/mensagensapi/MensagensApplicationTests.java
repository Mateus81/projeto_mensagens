package io.github.mateus81.mensagensapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import io.github.mateus81.mensagensapi.config.TestConfig;

@SpringBootTest
@ContextConfiguration(classes = {MensagensApplication.class, TestConfig.class})
class MensagensApplicationTests {

	@Test
	void contextLoads() {
	}

}
