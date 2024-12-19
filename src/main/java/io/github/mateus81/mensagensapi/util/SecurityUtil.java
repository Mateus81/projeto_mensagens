package io.github.mateus81.mensagensapi.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@Component
public class SecurityUtil {
	
	private final UsuarioRepository usuarioRepository;
	
	// Construtor
	public SecurityUtil(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	public Integer getAuthenticatedId() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Usuario usuario = usuarioRepository.findByEmail(email);
		if(usuario != null) {
			return usuario.getId();
		} 
		throw new IllegalStateException("Usuário autenticado não encontrado");
	}
}
