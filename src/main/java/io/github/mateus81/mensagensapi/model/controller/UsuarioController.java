package io.github.mateus81.mensagensapi.model.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.UsuarioService;

@CrossOrigin("*")
@RestController
public class UsuarioController {

	private final UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	// Busca todos os usuários
	@GetMapping("/usuarios")
	public List<Usuario> getAllUsers() {
		return usuarioService.getAllUsers();
	}
	
	// Busca usuário por Id
	@GetMapping("/usuarios/{id}")
	public Usuario getUserById(Integer id) {
		return usuarioService.getUserById(id);
	}
	
	// Cria um usuário
	@PostMapping("/usuarios")
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario registerUser(@RequestBody Usuario usuario) {
		return usuarioService.registerUser(usuario);
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
		if(usuarioService.existsById(id)) {
			Usuario usuarioExistente = usuarioService.getUserById(id);
			
			usuarioExistente.setNome(usuarioAtualizado.getNome());
			usuarioExistente.setEmail(usuarioAtualizado.getEmail());
			
			// Verifica se a foto deve ser atualizada
			if(usuarioAtualizado.getfoto()!= null) {
				usuarioExistente.setFoto(usuarioAtualizado.getfoto());
			}
			
			return usuarioService.saveOrUpdateUser(usuarioExistente);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado");
		}
		
	}
}
