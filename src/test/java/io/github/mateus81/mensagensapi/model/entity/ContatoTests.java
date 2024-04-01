package io.github.mateus81.mensagensapi.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

// Testes do Contato
public class ContatoTests {

	@Test
	public void testGettersAndSetters() {
		Contato contato = new Contato();
		contato.setId(1);
		contato.setNome("Marcos");
		contato.setTelefone("21 91000-0000");
		
		// Verificação
		assertEquals(1, contato.getId());
		assertEquals("Marcos", contato.getNome());
		assertEquals("21 91000-0000", contato.getTelefone());
	}
	
	// Realiza testes com valores inválidos
	
}
