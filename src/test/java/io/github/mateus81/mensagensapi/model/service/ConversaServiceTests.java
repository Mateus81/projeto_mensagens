package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;

@ExtendWith(MockitoExtension.class)
public class ConversaServiceTests {

	@InjectMocks
	private ConversaService conversaService;

	@Mock
	private ConversaRepository conversaRepository;
	
	@Test
	public void testReadAllConversas() {
		Conversa conversa = new Conversa();
		Conversa conversa2 = new Conversa();
		List<Conversa> conversas = Arrays.asList(conversa, conversa2);
		when(conversaRepository.findAll()).thenReturn(conversas);
		List<Conversa> conversaResult = conversaService.readAllConversas();
		assertEquals(conversaResult, conversas);
	}
	
	@Test
	public void testReadConversaById() {
		Conversa conversa = new Conversa(1);
		when(conversaRepository.findById(anyInt())).thenReturn(Optional.of((conversa)));
		Conversa conversaResult = conversaService.readConversaById(1);
		assertEquals(conversaResult, conversa);
	}
	
	@Test
	public void testDeleteConversaById() {
		Conversa conversa = new Conversa(2);
		when(conversaRepository.findById(anyInt())).thenReturn(Optional.of(conversa));
		conversaService.deleteConversaById(2);
		verify(conversaRepository).delete(conversa);
	}
	
	@Test 
	public void testStartConversa() {
		Conversa conversa = new Conversa(3);
		when(conversaRepository.save(conversa)).thenReturn(conversa);
		Conversa conversaResult = conversaService.startConversa(conversa);
		assertEquals(conversaResult, conversa);
	}
	
	@Test
	public void testEndConversa() {
		Conversa conversa = new Conversa(4);
		when(conversaRepository.findById(anyInt())).thenReturn(Optional.of(conversa));
		when(conversaRepository.save(conversa)).thenReturn(conversa);
		Conversa conversaResult = conversaService.endConversa(4);
		assertEquals(conversaResult, conversa);
	}
}
