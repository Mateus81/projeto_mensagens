package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.mateus81.mensagensapi.model.dto.LoginRequest;
import io.github.mateus81.mensagensapi.model.dto.UsuarioDTO;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.UsuarioService;
import io.github.mateus81.mensagensapi.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTests {

	@InjectMocks
	private UsuarioController usuarioController;
	
	@Mock
	private UsuarioService usuarioService;
	
	@Mock
	private JwtUtil jwtUtil;
	
	@Test
	public void testGetAllUsers() {
		// Cria dois usuários e os insere numa lista
		Usuario usuario = new Usuario();
		Usuario usuario2 = new Usuario();
		List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
		// Cria lista DTOs e retorna com os dados de cada usuário
		List<UsuarioDTO> Dtos = usuarios.stream().map(usuarioEntity -> {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setId(usuarioEntity.getId());
			dto.setNome(usuarioEntity.getNome());
			dto.setEmail(usuarioEntity.getEmail());
			dto.setSenhaNaoProtegida(usuarioEntity.getSenha());
			return dto;
		}).collect(Collectors.toList());
		// Mocka, executa e verifica
		when(usuarioService.getAllUsers()).thenReturn(usuarios);
		List<UsuarioDTO> usuarioResult = usuarioController.getAllUsers();
		assertEquals(usuarioResult, Dtos);
	}
	
	@Test
	public void testGetUserById() throws Exception {
		// Cria usuário e usuário dto e iguala seus IDs
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
		// Cria usuário e dto de usuário e coloca nome e Id correspondentes
		Usuario usuario = new Usuario(1, "Daniel");
		UsuarioDTO dto = new UsuarioDTO();
		dto.setNome(usuario.getNome());
		dto.setId(usuario.getId());
		// Mocka, executa e verifica
		when(usuarioService.getUserByNome("Daniel")).thenReturn(usuario);
		UsuarioDTO result = usuarioController.getUserByNome("Daniel");
		assertEquals(result.getId(), dto.getId());
		assertEquals(result.getNome(), dto.getNome());
	}
	
	@Test
	public void testGetUserByEmail() throws Exception {
		Usuario usuario = new Usuario();
		usuario.setEmail("test@gmail.com");
		when(usuarioService.getUserByEmail(usuario.getEmail())).thenReturn(usuario);
		ResponseEntity<Usuario> result = usuarioController.getUserByEmail(usuario.getEmail());
		assertEquals(result.getStatusCode(), HttpStatus.OK);
		assertEquals(usuario, result.getBody());
	}
	
	@Test
	public void testDeleteUserById() {
		// Cria usuário
		Usuario usuario = new Usuario(1, "Marcos");
		// Deleta
		doNothing().when(usuarioService).deleteUserById(anyInt());
		usuarioController.deleteUserById(1);
		// Verifica
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

        // Executa
        Usuario result = usuarioController.registerUser(usuarioDto);

        // Assert
        assertEquals(usuario.getId(), result.getId());
        assertEquals(usuario.getNome(), result.getNome());
        assertEquals(usuario.getEmail(), result.getEmail());
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		// Cria usuário
		Usuario usuarioAtualizado = new Usuario(1, "Mateus");
		// Mocks
		when(usuarioService.getUserById(1)).thenReturn(usuarioAtualizado);
		when(usuarioService.saveOrUpdateUser(usuarioAtualizado)).thenReturn(usuarioAtualizado);
		when(usuarioService.existsById(1)).thenReturn(true);
		// Execução e verificação
		Usuario response = usuarioController.updateUser(1, usuarioAtualizado);
		assertEquals(response, usuarioAtualizado);
	}
	
	@Test
	public void testLogin() throws Exception {
		// Cria e-mail, senha, usuário e atribui valores para teste de login
        String email = "test@example.com";
        String senha = "password";
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setNome("Test User");
        // Mocks
        when(usuarioService.auth(email, senha)).thenReturn(usuario);
        when(jwtUtil.generateToken(email)).thenReturn("mock-token");
        // Pedido de login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setSenha(senha);
        // Execução
        ResponseEntity<UsuarioDTO> result = usuarioController.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        
        UsuarioDTO dto = result.getBody();
        assertEquals(usuario.getEmail(), dto.getEmail());
        assertEquals(usuario.getNome(), dto.getNome());
        assertEquals("mock-token", dto.getToken());
    }
}
