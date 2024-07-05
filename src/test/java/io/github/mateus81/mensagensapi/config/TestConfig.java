package io.github.mateus81.mensagensapi.config;

import java.util.ArrayList;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.github.mateus81.mensagensapi.model.service.UserDetailsServiceImpl;

@TestConfiguration
public class TestConfig {
	
	@Bean
	 UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl() {
		@Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
               // Retorne um usuário falso ou simulado para os testes
               return new org.springframework.security.core.userdetails.User(
                       "testUser", 
                       "password", 
                       new ArrayList<>());
			}
		};
	}
}
