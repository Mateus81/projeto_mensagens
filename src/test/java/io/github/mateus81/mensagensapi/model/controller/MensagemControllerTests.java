package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

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
	public void testCreateMessage() {
		Mensagem mensagem = new Mensagem();
		when(mensagemService.createMessage(mensagem)).thenReturn(mensagem);
		Mensagem mensagemResult = mensagemController.createMessage(mensagem);
		assertEquals(mensagemResult, mensagem);
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
