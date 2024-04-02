package io.github.mateus81.mensagensapi.model.entity;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MensagemTests {
	
	// Testa se a mensagem é criada com sucesso 
	@Test
	public void testMessageCreation() {
		Mensagem mensagem = new Mensagem();
		Assertions.assertNotNull(mensagem);
		Assertions.assertNull(mensagem.getId());
		Assertions.assertNull(mensagem.getTexto());
	}
	
	// Testa setters/getters
	@Test
	public void testSetAndGet() {
		Mensagem mensagem = new Mensagem();
		mensagem.setId(1);
		mensagem.setTexto("Olá, mundo!");
		mensagem.setData_hora_envio(new Date());
		
		Assertions.assertEquals(1, mensagem.getId());
		Assertions.assertEquals("Olá, mundo!", mensagem.getTexto());
		Assertions.assertNotNull(mensagem.getDataHoraEnvio());
	}
	
	// Testa mensagem entre usuarios
	@Test
	public void testSetAndGetUsuario() {
		Usuario remetente = new Usuario("Marcos", "marcos@gmail.com");
		Usuario destino = new Usuario("Fernando","fernando@yahoo.com");
		Mensagem mensagem = new Mensagem();
		// Insere ambos na mensagem
		mensagem.setUsuariodestino(destino);
		mensagem.setUsuarioremetente(remetente);
		// Verificação
		Assertions.assertEquals(remetente, mensagem.getUsuarioremetente());
		Assertions.assertEquals(destino, mensagem.getUsuariodestino());
	}
	
	// Testa mensagem vista na conversa
	@Test
	public void testSetGetVistaAndConversa() {
		// Cria instancias de mensagem e conversa
		Mensagem mensagem = new Mensagem();
		Conversa conversa = new Conversa();
		// Estabelece a relação
		mensagem.setVista(true);
		mensagem.setConversa(conversa);
		// Verifica
		Assertions.assertTrue(mensagem.isVista());
		Assertions.assertEquals(conversa, mensagem.getConversa());
	}
}
