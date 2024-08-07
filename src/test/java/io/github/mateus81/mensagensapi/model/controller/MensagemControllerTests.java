package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.github.mateus81.mensagensapi.model.dto.MensagemDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.service.MensagemService;

@ExtendWith(MockitoExtension.class)
public class MensagemControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	 @BeforeEach
	 public void setup() {
		 mockMvc = MockMvcBuilders.standaloneSetup(new MensagemController(mensagemService)).build();
	    }
	
	@InjectMocks
	private MensagemController mensagemController;

	@Mock
	private MensagemService mensagemService;
	
	@Test
	public void TestReadMessageById() {
		Mensagem mensagem = new Mensagem();
		mensagem.setId(1);
		mensagem.setTexto("Olá");
		MensagemDTO dto = new MensagemDTO();
		dto.setId(mensagem.getId());
		dto.setTexto(mensagem.getTexto());
		when(mensagemService.getMessageById(anyInt())).thenReturn(mensagem);
		MensagemDTO mensagemResult = mensagemController.readMessage(1);
		assertEquals(mensagemResult.getId(), dto.getId());
		assertEquals(mensagemResult.getTexto(), dto.getTexto());
	}
	
	@Test
	public void testGetMensagensByConversaId() {
		// Criação da conversa e das mensagens
		Conversa conversa = new Conversa(1);
		Mensagem mensagem = new Mensagem();
		Mensagem mensagem2 = new Mensagem();
		mensagem2.setTexto("Como vai?");
		mensagem.setTexto("Olá");
		List<Mensagem> mensagens = Arrays.asList(mensagem, mensagem2);
		conversa.setMensagens(mensagens);
		when(mensagemService.getMensagensByConversaId(conversa.getId())).thenReturn(mensagens);
		List<Mensagem> result = mensagemController.getMensagensByConversaId(conversa.getId());
		assertEquals(result, mensagens);
	}
	
	@Test
	public void testCreateMessage() throws Exception {
		 // Cria conversa
	     Conversa conversa = new Conversa(1);
	     
		 // Crie um DTO de exemplo e setamos conversa
		 MensagemDTO dto = new MensagemDTO();
		 dto.setconversa(conversa);
		 dto.setId(1);
	     dto.setTexto("Teste de mensagem");
	     
	    // Crie um objeto Mensagem esperado copiando dados de DTO
	    Mensagem expectedMensagem = new Mensagem();
	    expectedMensagem.setConversa(dto.getConversa());
	    expectedMensagem.setId(dto.getId());
		expectedMensagem.setTexto(dto.getTexto());
		
	    // Stub o método createMessage do MensagemService
	    when(mensagemService.createMessage(conversa.getId(), dto)).thenReturn(expectedMensagem);
	    
	    // Chame o método createMessage do controller
	    Mensagem createdMessage = mensagemController.createMessage(conversa.getId(), dto);
	    // Verifique se o mensagemService.createMessage foi chamado com o argumento correto
	    verify(mensagemService).createMessage(conversa.getId(), dto);
	    
	    // Compare o objeto Mensagem recebido com o objeto Mensagem esperado
	    assertEquals(expectedMensagem, createdMessage);
	}
	
	@Test
	public void testDeleteMessageById() {
		Mensagem mensagem = new Mensagem(1, "Olá, Mundo", false);
		doNothing().when(mensagemService).deleteMessageById(anyInt());
		mensagemController.deleteMessageById(1);
		verify(mensagemService, times(1)).deleteMessageById(anyInt());
	}
	
	@Test 
	public void testUpdateMessage() {
		Mensagem mensagem = new Mensagem(1, "Meu amigo, e aí?", false);
		Mensagem mensagemAtualizada = new Mensagem(1, "Meu amigo, e aí, beleza?", false);
		when(mensagemService.updateMessage(1, mensagemAtualizada)).thenReturn(mensagemAtualizada);
		ResponseEntity<Mensagem> response = mensagemController.updateMessage(1, mensagemAtualizada);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mensagemAtualizada, response.getBody());
	}
	
	@Test
	public void testMarkMessageAsRead() throws Exception {
		MvcResult result = mockMvc.perform(patch("/conversas/1/mensagens/1")).andReturn();
		
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
		String expectedResponse = "Mensagem marcada como lida";
		assertEquals(expectedResponse, result.getResponse().getContentAsString());
		verify(mensagemService, times(1)).markAsRead(anyInt());
	}
	
	@Test
	public void testMarkAllAsRead() throws Exception {
        Integer conversaId = 1;
        // Define a resposta esperada
        String expectedResponse = "Todas as mensagens foram marcadas como lidas";
        // Realiza a chamada para o endpoint
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/conversas/" + conversaId + "/mensagens", "");
        // Verificações
        mockMvc.perform(requestBuilder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(expectedResponse));

        // Verifica se o método markAllAsRead foi chamado
        verify(mensagemService, times(1)).markAllAsRead(eq(conversaId));
    }

}
