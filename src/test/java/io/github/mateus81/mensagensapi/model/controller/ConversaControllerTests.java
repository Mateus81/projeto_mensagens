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

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.service.ConversaService;

@ExtendWith(MockitoExtension.class)
public class ConversaControllerTests {

	@InjectMocks
	private ConversaController conversaController;

	@Mock
	private ConversaService conversaService;
	
	@Test
	public void testGetAllConversas() {
		Conversa conversa = new Conversa();
		Conversa conversa2 = new Conversa();
		List<Conversa> conversas = Arrays.asList(conversa, conversa2);
		when(conversaService.readAllConversas()).thenReturn(conversas);
		List<Conversa> conversaResult = conversaController.getAllConversas();	
		assertEquals(conversaResult, conversas);
	}
	
	@Test
	public void testGetConversaById() {
		Conversa conversa = new Conversa(1);
		when(conversaService.readConversaById(anyInt())).thenReturn(conversa);
		Conversa conversaResult = conversaController.getConversaById(1);
		assertEquals(conversaResult, conversa);
	}
	
	@Test
	public void testDeleteConversaById() {
		Conversa conversa = new Conversa(1);
		doNothing().when(conversaService).deleteConversaById(anyInt());
		conversaController.deleteConversaById(1);
		verify(conversaService, times(1)).deleteConversaById(anyInt());
	}
	
	@Test
	public void testStartConversa() {
		Conversa conversa = new Conversa();
		when(conversaService.startConversa(conversa)).thenReturn(conversa);
		Conversa conversaResult = conversaController.startConversa(conversa);
		assertEquals(conversaResult, conversa);
}
	
	@Test
	public void testEndConversa() {
		Conversa conversa = new Conversa(1);
		when(conversaService.endConversa(conversa.getId())).thenReturn(conversa);
		ResponseEntity<String> response = conversaController.endConversa(conversa.getId());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Conversa encerrada com sucesso", response.getBody());
	}
}
