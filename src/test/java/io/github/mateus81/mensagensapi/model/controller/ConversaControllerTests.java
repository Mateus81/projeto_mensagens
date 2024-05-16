package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.mateus81.mensagensapi.model.dto.ConversaDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.ConversaService;

@ExtendWith(MockitoExtension.class)
public class ConversaControllerTests {

	@InjectMocks
	private ConversaController conversaController;

	@Mock
	private ConversaService conversaService;
	
	@Test
	public void testGetAllConversas() {
		// Criação do usuario e inserindo ele nas conversas
		Usuario usuario = new Usuario(1);
		Conversa conversa = new Conversa();
		conversa.setUsuario(usuario);
		Conversa conversa2 = new Conversa();
		conversa2.setUsuario(usuario);
		List<Conversa> conversas = Arrays.asList(conversa, conversa2);
		// Validação
		List<ConversaDTO> dtos = conversas.stream().map(conversaEntity -> {
			ConversaDTO dto = new ConversaDTO();
			dto.setId(conversaEntity.getId());
			dto.setUsuario(conversaEntity.getUsuario());
			return dto;
		}).collect(Collectors.toList());
		
		when(conversaService.readAllConversas()).thenReturn(conversas);
		List<ConversaDTO> conversaResult = conversaController.getAllConversas();	
		assertEquals(conversaResult, dtos);
	}
	
	@Test
	public void testGetConversaById() {
		// Define usuario, conversa e dto da conversa
		Usuario usuario = new Usuario(1);
		Conversa conversa = new Conversa(1);
		conversa.setUsuario(usuario);
		ConversaDTO dto = new ConversaDTO();
		dto.setId(conversa.getId());
		dto.setUsuario(conversa.getUsuario());
		
		when(conversaService.readConversaById(anyInt())).thenReturn(conversa);
		ConversaDTO conversaResult = conversaController.getConversaById(1);
		assertEquals(conversaResult, dto);
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
		// Cria um DTO de exemplo
		ConversaDTO dto = new ConversaDTO();
		dto.setId(1);
		dto.setUsuario(new Usuario());
		// Cria um objeto conversa
		Conversa conversa = new Conversa();
		conversa.setId(dto.getId());
		conversa.setUsuario(dto.getUsuario());
		
		when(conversaService.startConversa(any(Conversa.class))).thenReturn(conversa);
		Conversa createdConversa = conversaController.startConversa(dto);
		verify(conversaService).startConversa(any(Conversa.class));
		assertEquals(createdConversa, conversa);
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
