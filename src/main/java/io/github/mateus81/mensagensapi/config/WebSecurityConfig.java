package io.github.mateus81.mensagensapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {
	
	@Bean
	SecurityFilterChain security(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().permitAll().and().httpBasic().and().csrf().disable();
		
		return http.build();
	}

}