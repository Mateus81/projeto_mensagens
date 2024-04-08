package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.mateus81.mensagensapi.model.entity.Contato;
import io.github.mateus81.mensagensapi.model.service.ContatoService;

@ExtendWith(MockitoExtension.class)
public class ContatoControllerTests {

	@InjectMocks
	private ContatoController contatoController;

	@Mock
	private ContatoService contatoService;
	
	@Test
	public void testGetAllContatos() {
		Contato contato = new Contato();
		Contato contato2 = new Contato();
		List<Contato> contatos = Arrays.asList(contato, contato2);
		when(contatoService.readAllContatos()).thenReturn(contatos);
		List<Contato> contatoResult = contatoController.getAllContatos();
		assertEquals(contatoResult, contatos);
	}
	
	@Test
	public void testGetContatoById() {
		Contato contato = new Contato(1, "Matt");
		when(contatoService.readContatoById(anyInt())).thenReturn(contato);
		Contato contatoResult = contatoController.getContato(1);
		assertEquals(contatoResult, contato);
	}
	
	@Test
	public void testDeleteContatoById() {
		Contato contato = new Contato(1, "James");
		doNothing().when(contatoService).deleteContatoById(anyInt());
		contatoController.deleteContatoById(3);
		verify(contatoService, times(1)).deleteContatoById(anyInt());
	}
	
	@Test
	public void testInsertContato() {
		Contato contato = new Contato(1, "Felipe");
		when(contatoService.insertContato(contato)).thenReturn(contato);
		Contato contatoResult = contatoController.insertContato(contato);
		assertEquals(contatoResult, contato);
	}
	
	@Test 
	public void testUpdateContato() {
		Contato contatoAtualizado = new Contato(1, "Tomás de Aquino");
		when(contatoService.updateContato(1, contatoAtualizado)).thenReturn(contatoAtualizado);
		ResponseEntity<Contato> response = contatoController.updateContato(1, contatoAtualizado);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(contatoAtualizado, response.getBody());
	}
}
