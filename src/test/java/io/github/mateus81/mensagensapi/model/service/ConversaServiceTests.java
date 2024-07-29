package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.mateus81.mensagensapi.model.dto.ConversaDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;
import io.github.mateus81.mensagensapi.model.service.ConversaService.StatusConversa;

@ExtendWith(MockitoExtension.class)
public class ConversaServiceTests {

	@InjectMocks
	private ConversaService conversaService;

	@Mock
	private ConversaRepository conversaRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private SecurityContext security;
	
	@Mock
	private Authentication auth;
	
	@BeforeEach
	public void setUp() {
		SecurityContextHolder.setContext(security);
		when(security.getAuthentication()).thenReturn(auth);
	    }

	@Test
	public void testGetLoggedUserEmail() {
		
		UserDetails userDetails = User.withUsername("test@example.com").password("password").authorities("USER").build();
		when(auth.getPrincipal()).thenReturn(userDetails);
		
		String email = conversaService.getLoggedUserEmail();
		assertEquals("test@example.com", email);
	}
	
	@Test
	public void testReadAllConversasByUser() {
		// Cria email e autentica
		String email = "test@example.com";
		UserDetails userDetails = User.withUsername(email).password("password").authorities("USER").build();
		when(auth.getPrincipal()).thenReturn(userDetails);
		// Cria usuario e associa-o as conversas
		Usuario usuario = new Usuario(1);
		usuario.setEmail(email);
		Conversa conversa = new Conversa();
		Conversa conversa2 = new Conversa();
		conversa.setUsuario(usuario);
		conversa2.setUsuarioDest(usuario);
		List<Conversa> conversas = Arrays.asList(conversa, conversa2);
		
		when(usuarioRepository.findByEmail(email)).thenReturn(usuario);
		when(conversaRepository.findByUsuarioOrUsuarioDest(usuario, usuario)).thenReturn(conversas);
		when(conversaService.getLoggedUserEmail()).thenReturn(email);
		List<Conversa> conversaResult = conversaService.readAllConversasByUser();
		assertEquals(conversaResult, conversas);
	}
	
	@Test
	public void testReadConversaById() {
		// Cria email e autentica
		String email = "test@example.com";
		UserDetails userDetails = User.withUsername(email).password("password").authorities("USER").build();
		when(auth.getPrincipal()).thenReturn(userDetails);
		// Associa usuario a conversa
		Conversa conversa = new Conversa(1);
		Usuario usuario = new Usuario(1);
		usuario.setEmail(email);
		conversa.setUsuario(usuario);
		// Verifica
		when(conversaRepository.findById((conversa.getId()))).thenReturn(Optional.of((conversa)));
		Conversa conversaResult = conversaService.readConversaById(1);
		assertEquals(conversaResult, conversa);
	}
	
	@Test
	public void testDeleteConversaById() {
		// Cria email e autentica
		String email = "test@example.com";
		UserDetails userDetails = User.withUsername(email).password("password").authorities("USER").build();
		when(auth.getPrincipal()).thenReturn(userDetails);
		// Cria conversa e usuario associados
		Conversa conversa = new Conversa(2);
		Usuario usuario = new Usuario(1);
		usuario.setEmail(email);
		conversa.setUsuario(usuario);
		// Verifica
		when(conversaRepository.findById(anyInt())).thenReturn(Optional.of(conversa));
		// Deleta
		conversaService.deleteConversaById(2);
		verify(conversaRepository).delete(conversa);
	}
	
	@Test 
	public void testStartConversa() throws Exception {
		// Mesmo procedimento dos outros testes para evitar Potential Stubbing Problem
		UserDetails userDetails = User.withUsername("loggedUserEmail").password("password").authorities("USER").build();
        when(auth.getPrincipal()).thenReturn(userDetails);
		// Inicia usuario logado com seu email
        Usuario loggedUser = new Usuario();
        loggedUser.setEmail("loggedUserEmail");
        // Inicia usuario destino com seu nome 
        Usuario destinatario = new Usuario();
        destinatario.setNome("destinatarioNome");
        // Verifica se ambos existem e os retorna
        when(usuarioRepository.findByEmail("loggedUserEmail")).thenReturn(loggedUser);
        when(usuarioRepository.findByNome("destinatarioNome")).thenReturn(destinatario);
        // Cria DTO de conversa e destinatário e os anexa
        ConversaDTO conversaDTO = new ConversaDTO();
        Usuario destinatarioDto = new Usuario();
        destinatarioDto.setNome("destinatarioNome");
        conversaDTO.setUsuarioDest(destinatarioDto);
        // Salva e inicia conversa e seta usuario predefinido (destino)
        Conversa savedConversa = new Conversa();
        savedConversa.setUsuarioDest(destinatario);
        savedConversa.setStatus(StatusConversa.OPEN);
        savedConversa.setData_inicio(Date.from(Instant.now()));
        // Salva no repositorio
        when(conversaRepository.save(any(Conversa.class))).thenReturn(savedConversa);
        // Cria o resultado e manda ao service que inicie
        Conversa result = conversaService.startConversa(conversaDTO);
        // Compara
        assertNotNull(result);
        assertEquals(destinatario, result.getUsuarioDest());
        assertEquals(StatusConversa.OPEN, result.getStatus());
        assertNotNull(result.getData_inicio());
    }
	
	@Test
	public void testEndConversa() {
		// Cria email e autentica
		String email = "test@example.com";
		UserDetails userDetails = User.withUsername(email).password("password").authorities("USER").build();
		when(auth.getPrincipal()).thenReturn(userDetails);
		// Associa usuario a conversa
		Conversa conversa = new Conversa(4);
		Usuario usuario = new Usuario(1);
		usuario.setEmail(email);
		conversa.setUsuario(usuario);
		when(conversaRepository.findById(anyInt())).thenReturn(Optional.of(conversa));
		when(conversaRepository.save(conversa)).thenReturn(conversa);
		Conversa conversaResult = conversaService.endConversa(4);
		assertEquals(conversaResult, conversa);
	}
}
