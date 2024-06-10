package io.github.mateus81.mensagensapi.model.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	private final PasswordEncoder passwordEncoder;

	// Construtor
	public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// Busca todos os usuários
	public List<Usuario> getAllUsers() {
		return usuarioRepository.findAll();
	}

	// Busca um usuário pelo ID
	public Usuario getUserById(Integer id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
	}
	        
	// Salva/Atualiza um usuário - neste caso utilizamos o ID existente apesar do
	// mesmo código de registerUser
	@Transactional
	public Usuario saveOrUpdateUser(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	// Deleta um usuário
	@Transactional
	public void deleteUserById(Integer id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		usuarioRepository.delete(usuario);
	}

	// Cadastra um usuário
	@Transactional
	public Usuario registerUser(Usuario usuario, String senhaNaoProtegida) {
		if(usuario == null) {
			throw new IllegalArgumentException("Usuário não pode ser nulo");
		}
		usuario.setSenha(passwordEncoder.encode(senhaNaoProtegida));
		return usuarioRepository.save(usuario);
	}

	// Verifica se usuário existe pelo ID
	public boolean existsById(Integer id) {
		return usuarioRepository.findById(id).isPresent();
	}
	
	// Autentica usuario
	public Usuario auth(String email, String senhaNaoProtegida) {
		Usuario usuario = usuarioRepository.findByEmail(email);
		if(usuario != null && passwordEncoder.matches(senhaNaoProtegida, usuario.getSenha())) {
			return usuario;
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
	}
}
