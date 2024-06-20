package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.dto.LoginRequest;
import io.github.mateus81.mensagensapi.model.dto.UsuarioDTO;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTests {

	@InjectMocks
	private UsuarioController usuarioController;
	
	@Mock
	private UsuarioService usuarioService;
	
	@Test
	public void testGetAllUsers() {
		Usuario usuario = new Usuario();
		Usuario usuario2 = new Usuario();
		List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
		List<UsuarioDTO> Dtos = usuarios.stream().map(usuarioEntity -> {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setId(usuarioEntity.getId());
			dto.setNome(usuarioEntity.getNome());
			dto.setEmail(usuarioEntity.getEmail());
			dto.setSenhaNaoProtegida(usuarioEntity.getSenha());
			return dto;
		}).collect(Collectors.toList());
		
		when(usuarioService.getAllUsers()).thenReturn(usuarios);
		List<UsuarioDTO> usuarioResult = usuarioController.getAllUsers();
		assertEquals(usuarioResult, Dtos);
	}
	
	@Test
	public void testGetUserById() throws Exception {
		Usuario usuario = new Usuario(1);
		UsuarioDTO usuarioDto = new UsuarioDTO();
		usuarioDto.setId(usuario.getId());

	    // Mocke o comportamento do método getUserDtoById do UsuarioService
	    when(usuarioService.getUserById(anyInt())).thenReturn(usuario);

	    // Chame o método getUserById com o ID 1
	    UsuarioDTO usuarioResult = usuarioController.getUserById(1);

	    // Verifique se o resultado corresponde ao usuarioDto fictício
	    assertEquals(usuarioResult, usuarioDto);
	}
	
	@Test
	public void testGetUserByNome() throws Exception {
		Usuario usuario = new Usuario(1, "Daniel");
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNome(usuario.getNome());
		dto.setId(usuario.getId());
		when(usuarioService.getUserByNome("Daniel")).thenReturn(usuario);
		UsuarioDTO result = usuarioController.getUserByNome("Daniel");
		assertEquals(result.getId(), dto.getId());
		assertEquals(result.getNome(), dto.getNome());
	}
	
	@Test
	public void testDeleteUserById() {
		Usuario usuario = new Usuario(1, "Marcos");
		doNothing().when(usuarioService).deleteUserById(anyInt());
		usuarioController.deleteUserById(1);
		verify(usuarioService, times(1)).deleteUserById(anyInt());
	}
	
	@Test
	public void testRegisterUser() throws Exception {
		// Criação dos objetos
        UsuarioDTO usuarioDto = new UsuarioDTO();
        usuarioDto.setNome("Test User");
        usuarioDto.setEmail("test@example.com");
        usuarioDto.setSenhaNaoProtegida("password");

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Test User");
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        
        // Isso gera Stubbing problem -> when(usuarioService.registerUser(usuario, "password")).thenReturn(usuario);
        when(usuarioService.registerUser(any(Usuario.class), any(String.class))).thenReturn(usuario);

        Usuario result = usuarioController.registerUser(usuarioDto);

        // Assert
        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getNome(), result.getNome());
        assertEquals(usuario.getEmail(), result.getEmail());
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		Usuario usuarioAtualizado = new Usuario(1, "Mateus");
		when(usuarioService.getUserById(1)).thenReturn(usuarioAtualizado);
		when(usuarioService.saveOrUpdateUser(usuarioAtualizado)).thenReturn(usuarioAtualizado);
		when(usuarioService.existsById(1)).thenReturn(true);
		Usuario response = usuarioController.updateUser(1, usuarioAtualizado);
		assertEquals(response, usuarioAtualizado);
	}
	
	@Test
	public void testLogin() throws Exception {
        String email = "test@example.com";
        String senha = "password";
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail(email);
        usuario.setNome("Test User");
        usuario.setSenha(senha);

        when(usuarioService.auth(email, senha)).thenReturn(usuario);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setSenha(senha);
        
        UsuarioDTO result = usuarioController.login(loginRequest);

        // Assert
        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuario.getNome(), result.getNome());
    }
}
