package io.github.mateus81.mensagensapi.model.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import io.github.mateus81.mensagensapi.model.dto.RefreshTokenRequest;
import io.github.mateus81.mensagensapi.model.dto.TokenResponse;
import io.github.mateus81.mensagensapi.model.dto.UsuarioDTO;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.UsuarioService;
import io.github.mateus81.mensagensapi.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;

@CrossOrigin("*")
@RestController
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private JwtUtil jwtUtil;

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
	
	// Busca usuário por e-mail
	@GetMapping("/usuarios/email/{email}")
	public ResponseEntity<Usuario> getUserByEmail(@PathVariable String email) {
		try {
			Usuario usuario = usuarioService.getUserByEmail(email);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
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
	public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequest login) {
		Usuario usuario = usuarioService.auth(login.getEmail(), login.getSenha());
		if(usuario != null) {
			String token = jwtUtil.generateToken(usuario.getEmail());
			return ResponseEntity.ok().body(new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), token));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	// Método de logout explícito
	@PostMapping("/usuarios/logout")
	public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("message", "logout realizado com sucesso");
		return ResponseEntity.ok(responseBody);
	}
	
	// Método de refresh token para usuário
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) throws Exception {
		try {
			String newAccessToken = jwtUtil.refreshAccessToken(request.getRefreshToken());
			return ResponseEntity.ok(new TokenResponse(newAccessToken));
		} catch(ExpiredJwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
		}	
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
