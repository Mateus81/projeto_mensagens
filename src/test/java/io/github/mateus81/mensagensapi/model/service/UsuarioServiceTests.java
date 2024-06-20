package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {

	@InjectMocks
	private UsuarioService usuarioService;

	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void testGetAllUsers() {
		// Cria usuarios e os coloca na lista
		Usuario usuario1 = new Usuario("user1", "user@email.com");
		Usuario usuario2 = new Usuario("user2", "user2@email.com");
		List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
		// Verifica se o método funciona
		when(usuarioRepository.findAll()).thenReturn(usuarios);
		List<Usuario> usuariosResult = usuarioService.getAllUsers();
		assertEquals(usuariosResult, usuarios);
	}
	
	@Test
	public void testGetUserById() {
		// Cria usuário e o resultado esperado
		Usuario usuarioMock = new Usuario(4, "Renato");
		when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioMock));
		Usuario usuarioResult = usuarioService.getUserById(4);
		assertEquals(usuarioResult, usuarioMock);
	}
	
	@Test
	public void testGetUserByNome() {
		// Cria usuario e o resultado esperado
		Usuario usuarioMock = new Usuario(1, "Matt");
		when(usuarioRepository.findOptionalByNome(usuarioMock.getNome())).thenReturn(Optional.of(usuarioMock));
		Usuario result = usuarioService.getUserByNome(usuarioMock.getNome());
		assertEquals(result, usuarioMock);
	}
	
	@Test
	public void testSaveOrUpdateUser() {
		// Cria usuario e salva
		Usuario usuario = new Usuario("user", "user@gmail.com");
		when(usuarioRepository.save(usuario)).thenReturn(usuario);
		// Verifica o método
		Usuario usuarioResult = usuarioService.saveOrUpdateUser(usuario);
		assertEquals(usuarioResult, usuario);
	}
	
	@Test
	public void testDeleteUserById() {
		// Cria usuario
		Usuario usuario = new Usuario(5, "Leonor");
		when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
		// Operação de exclusão
		usuarioService.deleteUserById(5);
		verify(usuarioRepository).delete(usuario);		
	}
	
	@Test
	public void testRegisterUser() {
		// Define usuário para registro
		Usuario usuario = new Usuario(6, "Felipe", "Senha_do_felipe");
		Usuario usuarioSalvo = new Usuario(6, "Felipe", "SenhaProtegida");
		
		// Moca o Password Encoder
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
		String senhaProtegida = "SenhaProtegida";
		when(passwordEncoder.encode("Senha_do_felipe")).thenReturn(senhaProtegida);
		
		// Moca Usuario Repository
		UsuarioRepository repository = mock(UsuarioRepository.class);
		when(repository.save(any(Usuario.class))).thenAnswer((Answer<Usuario>) invocationOnMock -> {
			Usuario argument = invocationOnMock.getArgument(0);
			assertEquals("SenhaProtegida", argument.getSenha());
			return usuarioSalvo;
		});
		
		// Injeta os mocks no usuarioService
		UsuarioService usuarioService = new UsuarioService(repository, passwordEncoder);
		// Registra usuario
		Usuario usuarioRegistrado = usuarioService.registerUser(usuario, "Senha_do_felipe");
		// Verifica os resultados
		assertEquals(usuarioRegistrado, usuarioSalvo);
	}
	
	@Test
	public void testExistsById() {
		// Define usuario existente
		Usuario usuarioExistente = new Usuario(9, "Leão", "212121");
		// Define um usuario inexistente
		Usuario usuarioInexistente = new Usuario(10, "Ambrósio", "313131");
		
		when(usuarioRepository.findById(9)).thenReturn(Optional.of(usuarioExistente));
		when(usuarioRepository.findById(10)).thenReturn(Optional.empty());
		// Verifica
		assertTrue(usuarioService.existsById(9));
		assertFalse(usuarioService.existsById(10));
	}
	
	@Test 
	public void testAuthUser() {
		 // Arrange
		String email = "test@example.com";
		String senha = "password";
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		usuario.setSenha(passwordEncoder.encode(senha));

		when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
		when(passwordEncoder.matches(senha, usuario.getSenha())).thenReturn(true);

		// Act
		Usuario result = usuarioService.auth(email, senha);

		// Assert
		assertEquals(usuario, result);
	}
}
