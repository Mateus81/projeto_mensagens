package io.github.mateus81.mensagensapi.config;

import java.util.ArrayList;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;
import io.github.mateus81.mensagensapi.model.service.UserDetailsServiceImpl;

@TestConfiguration
public class TestConfig {
	
	@Bean
	@Primary
	UsuarioRepository mockRepository() {
		UsuarioRepository mockRepo = Mockito.mock(UsuarioRepository.class);
		Mockito.when(mockRepo.findByEmail("testUser")).thenReturn(new Usuario("testUser", "password", "test@test.com"));
		return mockRepo;
		
	}
	
	@Bean
	@Primary
	 UserDetailsService userDetailsService(UsuarioRepository repository) {
		return new UserDetailsServiceImpl() {
		@Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Usuario usuario = repository.findByEmail(username);
			if(usuario == null) {
				throw new UsernameNotFoundException("Usuário não encontrado" + username);
			}
               // Retorne um usuário falso ou simulado para os testes
               return new org.springframework.security.core.userdetails.User(
                       usuario.getEmail(), 
                       usuario.getSenha(),
                       new ArrayList<>());
			}
		};
	}
}
