package io.github.mateus81.mensagensapi.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	@Test
	public void testInvalidValues() {
		assertThrows(IllegalArgumentException.class, () -> new Contato("", "21 92000-0000"));
	}
	
	// Testes Hashcode
	@Test
	public void testSimilarHashcode() {
		Contato contato1 = new Contato("Daniel", "21 9300-0000");
		Contato contato2 = new Contato("Daniel", "21 9300-0000");
		
		assertEquals(contato1.hashCode(), contato2.hashCode());
	}
	
	@Test
	public void testDifferentHashcode() {
		Contato contato1 = new Contato("Daniel", "21 9300-0000");
		Contato contato2 = new Contato("Bruno", "21 9400-0000");
		
		assertNotEquals(contato1.hashCode(), contato2.hashCode());
	}
	
	@Test
	public void testHashCodeConsistency() {
		Contato contato = new Contato("Diego", "21 9500-0000");
		int originalHashcode = contato.hashCode();
		
		contato.setNome("Manuel");
		assertNotEquals(originalHashcode, contato.hashCode());
	}
}
