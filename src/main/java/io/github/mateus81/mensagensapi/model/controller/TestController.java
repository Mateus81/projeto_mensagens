package io.github.mateus81.mensagensapi.model.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.mateus81.mensagensapi.model.dto.UsuarioDTO;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@RestController
public class TestController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping("/user")
	public UsuarioDTO getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Usuario usuario = usuarioRepository.findByEmail(email);
		return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
	}
}
