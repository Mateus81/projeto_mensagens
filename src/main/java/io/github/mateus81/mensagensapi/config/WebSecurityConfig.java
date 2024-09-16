package io.github.mateus81.mensagensapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import io.github.mateus81.mensagensapi.model.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private PasswordEncoder appPasswordEncoder;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .authorizeRequests(authorizeRequests -> authorizeRequests
            	.antMatchers("/usuarios", "usuarios/login").permitAll()
                .antMatchers("/public/**").permitAll() // Permitir acesso público para determinados endpoints
                .antMatchers("/conversas/**").authenticated()
                .anyRequest().authenticated() // Requer autenticação para todos os outros endpoints
            )
            .httpBasic()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .invalidateHttpSession(true) // invalida a sessão durante o logout
            .clearAuthentication(true) // Limpa a autenticação do securityContext
            .permitAll().and().headers().cacheControl().disable().and().headers()
            .addHeaderWriter((request, response)-> {
            	response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            	response.setHeader("Pragma", "no-cache");
            	response.setHeader("Expires", "0");
            });
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
}


