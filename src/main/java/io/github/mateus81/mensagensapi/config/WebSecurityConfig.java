package io.github.mateus81.mensagensapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {
	
	@Bean
	SecurityFilterChain security(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
		.httpBasic().and().csrf().disable().cors().and().logout()
		.logoutUrl("/logout").logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()).permitAll();	
		return http.build();
	}	
	
	 @Bean
	 CorsFilter corsFilter() {
		 CorsConfiguration config = new CorsConfiguration();
		 config.setAllowCredentials(true);
		 config.addAllowedOrigin("http://localhost:4200");
		 config.addAllowedHeader("*");
		 config.addAllowedMethod("*");
		 
		 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		 source.registerCorsConfiguration("/**", config);
		 return new CorsFilter(source);
	}

}
