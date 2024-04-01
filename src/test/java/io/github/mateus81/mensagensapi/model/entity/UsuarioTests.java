package io.github.mateus81.mensagensapi.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UsuarioTests {
	

	@Test
	public void testGettersAndSetters() {
		Usuario usuario = new Usuario();
		usuario.setNome("Mateus");
		usuario.setId(1);
		usuario.setEmail("matt@tutanota.com");

		// Verifica se os dados são pertinentes
		assertEquals(1, usuario.getId());
		assertEquals("Mateus", usuario.getNome());
		assertEquals("matt@tutanota.com", usuario.getEmail());
	}
	
	// Realiza teste com valores inválidos
	@Test
	public void invalidValueTest() {
		assertThrows(IllegalArgumentException.class, () -> new Usuario("Marcelo", "Marcelo.com"));
	}
	
	// Testes Instancias Hashcode
	// Equivalentes
	@Test
	public void testSimilarHashcode() {
		Usuario usuario1 = new Usuario("Mateus", "matt@tutanota.com");
	    Usuario usuario2 = new Usuario("Mateus", "matt@tutanota.com");
	    
	    assertEquals(usuario1.hashCode(), usuario2.hashCode());
	}
	// Diferentes
	@Test
	public void testDifferentHashcode() {
		Usuario usuario1 = new Usuario("Mateus", "matt@tutanota.com");
		Usuario usuario2 = new Usuario("Maria", "maria@gmail.com");
		
		assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
	}
	// Consistencia
	@Test
	public void TestHashcodeConsistency() {
		Usuario usuario = new Usuario("Gabriel", "Gabriel@gmail.com");
		int originalHashcode = usuario.hashCode();
		
		usuario.setNome("Miguel");
		
		assertNotEquals(originalHashcode, usuario.hashCode());
	}
}
