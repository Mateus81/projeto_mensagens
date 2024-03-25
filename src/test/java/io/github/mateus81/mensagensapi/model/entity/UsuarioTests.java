package io.github.mateus81.mensagensapi.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

// Essa classe testará os getters e setters
@ExtendWith(MockitoExtension.class)
public class UsuarioTests {

	@Test
	public void testGettersAndSetters() {
		Usuario mockUsuario = new Usuario();
		mockUsuario.setNome("Mateus");
		mockUsuario.setId(1);
		mockUsuario.setEmail("matt@tutanota.com");

		// Verifica se os dados são pertinentes
		assertEquals(1, mockUsuario.getId());
		assertEquals("Mateus", mockUsuario.getNome());
		assertEquals("matt@tutanota.com", mockUsuario.getEmail());
	}
}
