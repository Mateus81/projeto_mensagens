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
import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.ConversaService;

@ExtendWith(MockitoExtension.class)
public class ConversaControllerTests {

	@InjectMocks
	private ConversaController conversaController;

	@Mock
	private ConversaService conversaService;
	
	@Test
	public void testGetConversasByUser() {
		// Criação do usuario/destino e inserindo eles nas conversas e as mensagens
		Usuario usuario = new Usuario(1);
		Usuario destino = new Usuario(2);
		Conversa conversa = new Conversa();
		Conversa conversa2 = new Conversa();
		conversa.setUsuario(usuario);
		conversa.setUsuarioDest(destino);
		conversa2.setUsuarioDest(destino);
		conversa2.setUsuario(usuario);
		Mensagem mensagem = new Mensagem(1,"Olá", false);
		// Seta mensagens para as conversas
		conversa.setMensagens(Arrays.asList(mensagem));
		conversa2.setMensagens(Arrays.asList(mensagem));
		List<Conversa> conversas = Arrays.asList(conversa, conversa2);
		
		ConversaController controller = new ConversaController(conversaService);
		// Validação
	    List<ConversaDTO> dtos = conversas.stream().map(conversaEntity -> {
	        ConversaDTO dto = new ConversaDTO();
	        dto.setId(conversaEntity.getId());
	        dto.setUsuario(conversaEntity.getUsuario());
	        dto.setUsuarioDest(conversaEntity.getUsuarioDest());
	        dto.setMensagens(controller.convertToDTO(conversaEntity.getMensagens()));
	        return dto;
	    }).collect(Collectors.toList());
		
		when(conversaService.readAllConversasByUser()).thenReturn(conversas);
		List<ConversaDTO> conversaResult = conversaController.getConversasByUser();	
		assertEquals(conversaResult, dtos);
	}
	
	@Test
	public void testGetConversaById() {
		// Define usuario, mensagem, conversa e dto da conversa
		Usuario usuario = new Usuario(1);
		Usuario destino = new Usuario(2);
		Mensagem mensagem = new Mensagem(1, "Olá", false);
		Conversa conversa = new Conversa(1);
		conversa.setUsuario(usuario);
		conversa.setUsuarioDest(destino);
		conversa.setMensagens(Arrays.asList(mensagem));
		ConversaController controller = new ConversaController(conversaService);
		ConversaDTO dto = new ConversaDTO();
		dto.setId(conversa.getId());
		dto.setUsuario(conversa.getUsuario());
		dto.setUsuarioDest(destino);
		dto.setMensagens(controller.convertToDTO(conversa.getMensagens()));
		
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
	public void testStartConversa() throws Exception {
		// Cria um DTO de exemplo
		ConversaDTO dto = new ConversaDTO();
		dto.setId(1);
		// Seta usuario
		Usuario usuario = new Usuario();
		dto.setUsuario(usuario);
		usuario.setNome("Destinatário");
		// Cria um objeto conversa
		Conversa conversa = new Conversa();
		conversa.setId(dto.getId());
		conversa.setUsuario(dto.getUsuario());
		
		when(conversaService.startConversa(any(ConversaDTO.class))).thenReturn(conversa);
		Conversa createdConversa = conversaController.startConversa(dto);
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
