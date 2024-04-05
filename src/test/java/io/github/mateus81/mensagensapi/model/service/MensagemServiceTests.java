package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.repository.MensagemRepository;

@ExtendWith(MockitoExtension.class)
public class MensagemServiceTests {

	@InjectMocks
	private MensagemService mensagemService;

	@Mock
	private MensagemRepository mensagemRepository;
	
	@Mock
	private EntityManager entityManager;
	
	@Mock 
	private Query query;
	
	@Test
	public void testGetMessageById() {
		Mensagem mensagem = new Mensagem(1, "Olá", false);
		when(mensagemRepository.findById(anyInt())).thenReturn(Optional.of(mensagem));
		Mensagem mensagemResult = mensagemService.getMessageById(1);
		assertEquals(mensagemResult, mensagem);
	}
	
	@Test
	public void testCreateMessage() {
		Mensagem mensagem = new Mensagem(1, "Olá", false);
		when(mensagemRepository.save(mensagem)).thenReturn(mensagem);
		Mensagem mensagemResult = mensagemService.createMessage(mensagem);
		assertEquals(mensagemResult, mensagem);
	}
	
	@Test
	public void testDeleteUserById() {
		Mensagem mensagem = new Mensagem(1, "Oi", false);
		when(mensagemRepository.findById(anyInt())).thenReturn(Optional.of(mensagem));
		mensagemService.deleteMessageById(1);
		verify(mensagemRepository).delete(mensagem);;
	}
	
	@Test
	public void testUpdateMessage() {
		Mensagem mensagem = new Mensagem(1, "Olá", false);
		Mensagem mensagemAtualizada = new Mensagem (1, "Ei!", false);
		
		when(mensagemRepository.findById(mensagemAtualizada.getId())).thenReturn(Optional.of(mensagem));
		when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagemAtualizada);
		Mensagem mensagemSalva = mensagemService.updateMessage(mensagemAtualizada.getId(), mensagemAtualizada);
		
		assertEquals(mensagemSalva.getId(), mensagem.getId());
		assertEquals(mensagemSalva.getTexto(), mensagemAtualizada.getTexto());
	}
	
	@Test
	public void testMarkAsRead() {
		Mensagem mensagem = new Mensagem(1, "Serei marcada como lida", false);
		when(mensagemRepository.findById(anyInt())).thenReturn(Optional.of(mensagem));
		mensagemService.markAsRead(1);
		verify(mensagemRepository).save(mensagem);
		assertTrue(mensagem.isVista());
	}
	
	@Test
	public void testMarkAllAsRead() {
		Conversa conversa = new Conversa(1);
		when(entityManager.createQuery(anyString())).thenReturn(query);
		// Injeta o entityManager no mensagemService
		ReflectionTestUtils.setField(mensagemService, "entityManager", entityManager);
		mensagemService.markAllAsRead(conversa.getId());
		String jpql = "UPDATE Mensagem m SET m.vista = true WHERE m.conversa.id = conversaId";
		verify(entityManager).createQuery(jpql); 
		verify(query).setParameter("conversaId", conversa.getId());
		verify(query).executeUpdate();
	}
}
