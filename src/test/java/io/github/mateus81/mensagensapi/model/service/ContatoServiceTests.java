package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.entity.Contato;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ContatoRepository;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class ContatoServiceTests {

	@InjectMocks
	private ContatoService contatoService;

	@Mock
	private ContatoRepository contatoRepository;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Test
	public void testReadContatoById() {
		// Define Contato
		Contato contato = new Contato(1, "Ana");
		when(contatoRepository.findById(anyInt())).thenReturn(Optional.of(contato));
		// Valida
		Contato contatoResult = contatoService.readContatoById(1);
		assertEquals(contatoResult, contato);
	}
	
	@Test
	public void testReadAllContatos() {
		// Cria Usuário 
		Usuario usuario = new Usuario(1);
		// Cria dois contatos e os insere numa lista e associa ao contato
		Contato contato1 = new Contato(2, "Marcelo");
		Contato contato2 = new Contato(3, "Leo");
		List<Contato> contatos = Arrays.asList(contato1, contato2);
		contato1.setUsuario(usuario);
		contato2.setUsuario(usuario);
		// Validação
		when(contatoRepository.findByUsuarioId(usuario.getId())).thenReturn(contatos);
		List<Contato> contatoResult = contatoService.readContatosByUsuario(usuario.getId());
		assertEquals(contatoResult, contatos);
	}
	
	@Test
	public void testInsertContato() {
		// Cria um usuário associado
		Usuario usuarioAssociado = new Usuario(2, "Renan");
		usuarioAssociado.setContatos(new ArrayList<>());
	    Usuario usuarioContato = new Usuario(3, "Fernando");

	    // Configura os mocks
	    when(usuarioRepository.findById(usuarioAssociado.getId())).thenReturn(Optional.of(usuarioAssociado));
	    when(usuarioRepository.findOptionalByNome(usuarioContato.getNome())).thenReturn(Optional.of(usuarioContato));
	    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAssociado);

	    // Chama o método insertContato
	    Usuario contatoResult = contatoService.insertContato(usuarioAssociado.getId(), usuarioContato);

	    // Verifica se o resultado é igual ao objeto Contato esperado
	    assertNotNull(contatoResult);
	    assertEquals(usuarioContato.getNome(), contatoResult.getNome());
	    assertTrue(usuarioAssociado.getContatos().contains(usuarioContato));

	    // Verifica se os métodos dos repositórios foram chamados corretamente
	    verify(usuarioRepository).findById(usuarioAssociado.getId());
	    verify(usuarioRepository).findOptionalByNome(usuarioContato.getNome());
	    verify(usuarioRepository).save(usuarioAssociado);
	}
	
	@Test
	public void testDeleteContato() {
		// Cria e verifica contato
		Contato contato = new Contato(5, "Paulo");
		when(contatoRepository.findById(anyInt())).thenReturn(Optional.of(contato));
		// Verifica
		contatoService.deleteContatoById(5);
		verify(contatoRepository).delete(contato);
	}
	
	@Test
	public void testUpdateContato() {
		// Cria contato existente 
		Contato contatoExistente = new Contato(6, "José");
		// Contato atualizado
		Contato contatoAtualizado = new Contato(6, "José Silveira");
		// Moca
		when(contatoRepository.findById(contatoAtualizado.getId())).thenReturn(Optional.of(contatoExistente));
		when(contatoRepository.save(any(Contato.class))).thenReturn(contatoExistente);
		Contato contatoSalvo = contatoService.updateContato(contatoAtualizado.getId(), contatoAtualizado);
		
		// Verifica
		assertEquals(contatoSalvo.getId(), contatoExistente.getId());
		assertEquals(contatoSalvo.getNome(), contatoAtualizado.getNome());
	}
}
