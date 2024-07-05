package io.github.mateus81.mensagensapi.model.entity;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mateus81.mensagensapi.model.service.ConversaService.StatusConversa;

public class ConversaTests {
	
	@Test
	public void testConstructorDefault() {
		Conversa conversa = new Conversa();
		Assertions.assertNotNull(conversa);
	}

	@Test
	public void testConstructorwithUsuario() {
		// Cria usuário e o insere na conversa
		Usuario usuario = new Usuario();
		usuario.setNome("Mateus");
		usuario.setEmail("matt@tutanota.com");
		Conversa conversa = new Conversa(usuario);
		// Verifica se a conversa está ativa com o usuário passado
		Assertions.assertNotNull(conversa);
		Assertions.assertNotNull(conversa.getUsuario());
		Assertions.assertEquals(usuario.getId(), conversa.getUsuario().getId());
	}
	
	// Verifica se o Identificador está correto
	@Test
	public void testGetId() {
		Conversa conversa = new Conversa();
		conversa.setId(1);
		Assertions.assertEquals(1, conversa.getId());
	}
	
	// Testa conversa com dois usuários
	@Test
	public void testConversa() {
		Usuario usuarioInit = new Usuario();
		usuarioInit.setNome("Mateus");
		
		Usuario usuarioDest = new Usuario();
		usuarioDest.setNome("Renan");
		
		Conversa conversa = new Conversa();
		conversa.setUsuario(usuarioInit);
		conversa.setUsuarioDest(usuarioDest);
		conversa.setStatus(StatusConversa.OPEN);
		conversa.setData_inicio(new Date());
		
		Assertions.assertEquals(usuarioInit, conversa.getUsuario());
		Assertions.assertEquals(usuarioDest, conversa.getUsuarioDest());
		Assertions.assertEquals(StatusConversa.OPEN, conversa.getStatus());
		Assertions.assertNotNull(conversa.getData_inicio());
	}
}
