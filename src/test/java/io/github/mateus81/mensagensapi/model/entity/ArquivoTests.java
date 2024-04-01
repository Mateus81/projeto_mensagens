package io.github.mateus81.mensagensapi.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

// Classe de Teste de Getters/Setters
public class ArquivoTests {
	
	@Test
	public void testGettersAndSetters() {
		Arquivo mockArquivo = new Arquivo();
		mockArquivo.setId(1);
		mockArquivo.setNome("foto");
		mockArquivo.setTipo("jpeg");
		
		// Verifica se os dados são condizentes
		assertEquals(1, mockArquivo.getId());
		assertEquals("foto", mockArquivo.getNome());
		assertEquals("jpeg", mockArquivo.getTipo());
	}
	
	@Test
    public void testNullValues() {
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(null);
		arquivo.setTipo(null);

	    assertNull(arquivo.getNome());
	    assertNull(arquivo.getTipo());
	}

	    @Test
	    public void testInvalidValues() {
	        assertThrows(IllegalArgumentException.class, () -> new Arquivo(-1, "arquivo", "png", (long) 3));
	    }
	}

