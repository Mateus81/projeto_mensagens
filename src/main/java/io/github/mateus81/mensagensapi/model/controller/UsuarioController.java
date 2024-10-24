package io.github.mateus81.mensagensapi.model.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.mateus81.mensagensapi.model.dto.LoginRequest;
import io.github.mateus81.mensagensapi.model.dto.UsuarioDTO;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.UsuarioService;

@CrossOrigin("*")
@RestController
public class UsuarioController {

	private final UsuarioService usuarioService;
	private final PasswordEncoder passwordEncoder;
	
	public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
		this.usuarioService = usuarioService;
		this.passwordEncoder = passwordEncoder;
	}

	// Busca todos os usuários
	@GetMapping("/usuarios")
	public List<UsuarioDTO> getAllUsers() {
		List<Usuario> usuarios = usuarioService.getAllUsers();
		return usuarios.stream().map(usuario -> {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setId(usuario.getId());
			dto.setEmail(usuario.getEmail());
			dto.setNome(usuario.getNome());
			dto.setSenhaNaoProtegida(usuario.getSenha());
			return dto;
		}).collect(Collectors.toList());
	}

	// Busca usuário por Id
	@GetMapping("/usuarios/{id}")
	public UsuarioDTO getUserById(@PathVariable Integer id) {
		Usuario usuario = usuarioService.getUserById(id);
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(usuario.getId());
		dto.setNome(usuario.getNome());
		dto.setEmail(usuario.getEmail());
		dto.setSenhaNaoProtegida(usuario.getSenha());
		return dto;
	}
	
	// Busca usuário por nome
	@GetMapping("/usuarios/nome/{nome}")
	public UsuarioDTO getUserByNome(@PathVariable String nome){
		Usuario usuario = usuarioService.getUserByNome(nome);
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(usuario.getId());
		dto.setNome(usuario.getNome());
		dto.setEmail(usuario.getEmail());
		dto.setSenhaNaoProtegida(usuario.getSenha());
		return dto;
		
	}

	// Cria um usuário
	@PostMapping("/usuarios")
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario registerUser(@RequestBody UsuarioDTO usuarioDto) {
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioDto.getNome());
		usuario.setEmail(usuarioDto.getEmail());
		usuario.setSenha(usuarioDto.getSenhaNaoProtegida());
		return usuarioService.registerUser(usuario, usuarioDto.getSenhaNaoProtegida());
	}
	
	// Autentica usuário
	@PostMapping("/usuarios/login")
	public UsuarioDTO login(@RequestBody LoginRequest login) {
		Usuario usuario = usuarioService.auth(login.getEmail(), login.getSenha());
		UsuarioDTO dto = new UsuarioDTO();
		dto.setId(usuario.getId());
		dto.setEmail(usuario.getEmail());
		dto.setNome(usuario.getNome());
		return dto;
	}

	// Deleta usuário por ID
	@DeleteMapping("/usuarios/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserById(@PathVariable Integer id) {
		usuarioService.deleteUserById(id);
	}

	// Atualiza um usuário
	@PutMapping("/usuarios/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Usuario updateUser(@PathVariable Integer id, @RequestBody Usuario usuarioAtualizado) {
		if (usuarioService.existsById(id)) {
			Usuario usuarioExistente = usuarioService.getUserById(id);

			usuarioExistente.setNome(usuarioAtualizado.getNome());
			usuarioExistente.setEmail(usuarioAtualizado.getEmail());

			// Verifica se a foto deve ser atualizada
			if (usuarioAtualizado.getfoto() != null) {
				usuarioExistente.setFoto(usuarioAtualizado.getfoto());
			}

			return usuarioService.saveOrUpdateUser(usuarioExistente);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
		}

	}
}
