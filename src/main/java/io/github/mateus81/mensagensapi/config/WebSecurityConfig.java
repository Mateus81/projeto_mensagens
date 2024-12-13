package io.github.mateus81.mensagensapi.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.mateus81.mensagensapi.model.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private PasswordEncoder appPasswordEncoder;

    /* Este código tratava da autenticação utilizando Basic Auth, porém devido a inúmeros problemas de 
     * armazenamento de sessões, mudamos para cookies */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .authorizeRequests(authorizeRequests -> authorizeRequests
            	.antMatchers("/usuarios", "/usuarios/login").permitAll()
                .antMatchers("/public/**").permitAll() // Permitir acesso público para determinados endpoints
                .antMatchers("/conversas/**").authenticated()
                .antMatchers("/contatos/**").authenticated()
                .anyRequest().authenticated() // Requer autenticação para todos os outros endpoints
            )
            .formLogin(form -> form
            		.loginProcessingUrl("/usuarios/login")
            		.successHandler((request, response, auth) -> {SecurityContextHolder.getContext().setAuthentication(auth);
            		response.setStatus(HttpServletResponse.SC_OK);
            		})
            		.usernameParameter("email")
            		.passwordParameter("senha")
            		.permitAll()
            		)
            .logout(logout -> logout.logoutUrl("/logout")
            		.invalidateHttpSession(true) // invalida sessão
            		.clearAuthentication(true)	// limpa autenticação
            		.addLogoutHandler((request, response, auth) -> SecurityContextHolder.clearContext())
            		.deleteCookies("JSESSIONID") // remove cookie da sessão
            		.permitAll()
            		)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS).
            		sessionFixation().newSession())
            .httpBasic().disable()
            .headers(headers -> headers.cacheControl().disable() 
            );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(appPasswordEncoder);
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
    
    // Impede Basic Auth no navegador
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    		throws ServletException, java.io.IOException {
    	
    	// Logs para depuração
    	System.out.println("Session ID: " + request.getRequestedSessionId());
    	System.out.println("Authorization Header: " + request.getHeader("Authorization"));
    	System.out.println("Current User: " + SecurityContextHolder.getContext().getAuthentication());
    	
    	String authorizationHeader = request.getHeader("Authorization");
    	
    	if(authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
    		System.out.println("Warning: Authorization header detected and ignored.");
    	}
    	if(SecurityContextHolder.getContext().getAuthentication() == null && request.getSession(false) != null) {
    		System.out.println("Forcing session-based authentication");
    	}
    	
    	filterChain.doFilter(request, response);
    }
}


