package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import io.github.mateus81.mensagensapi.model.dto.MensagemDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;
import io.github.mateus81.mensagensapi.model.repository.MensagemRepository;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class MensagemServiceTests {

	@InjectMocks
	private MensagemService mensagemService;

	@Mock
	private MensagemRepository mensagemRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private ConversaRepository conversaRepository;
	
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
	public void testGetMensagemByConversaId() {
		// Criação da conversa e das mensagens
		Conversa conversa = new Conversa(1);
		Mensagem mensagem = new Mensagem();
		Mensagem mensagem2 = new Mensagem();
		mensagem2.setTexto("Como vai?");
		mensagem.setTexto("Olá");
		List<Mensagem> mensagens = Arrays.asList(mensagem, mensagem2);
		conversa.setMensagens(mensagens);
		// Mocks
		when(mensagemRepository.findByConversaId(conversa.getId())).thenReturn(mensagens);
		List<Mensagem> result = mensagemService.getMensagensByConversaId(conversa.getId());
		// Verificações
		assertEquals(mensagens, result);
	}
	
	@Test
	public void testCreateMessage() throws Exception {
		// Criação dos usuarios e conversa e setando cada um a ela
		Usuario remetente = new Usuario();
		remetente.setEmail("remetente@gmail.com");
		Usuario destinatario = new Usuario();
		destinatario.setNome("Destino");
		Conversa conversa = new Conversa();
		conversa.setId(1);
		conversa.setUsuario(remetente);
		conversa.setUsuarioDest(destinatario);
		// Criação da mensagemDTO e setando conversa nela e usuarios
		MensagemDTO dto = new MensagemDTO();
		dto.setconversa(conversa);
		dto.setUsuarioRemetente(remetente);
		dto.setUsuarioDestino(destinatario);
		dto.setTexto("Olá");
		// Criação da Mensagem
		Mensagem mensagem = new Mensagem();
		mensagem.setData_hora_envio(new Date());
		mensagem.setTexto(dto.getTexto());
		mensagem.setConversa(conversa);
		mensagem.setUsuarioremetente(remetente);
		mensagem.setUsuariodestino(destinatario);
		mensagem.setVista(false);
		// Mocks
		when(usuarioRepository.findByEmail(remetente.getEmail())).thenReturn(remetente);
		when(conversaRepository.findById(conversa.getId())).thenReturn(Optional.of(conversa));
		when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);
		// Verificações
		Mensagem mensagemResult = mensagemService.createMessage(dto);
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
