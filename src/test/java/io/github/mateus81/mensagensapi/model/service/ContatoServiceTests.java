package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
		// Cria dois contatos e os insere numa lista
		Contato contato1 = new Contato(2, "Marcelo");
		Contato contato2 = new Contato(3, "Leo");
		List<Contato> contatos = Arrays.asList(contato1, contato2);
		// Validação
		when(contatoRepository.findAll()).thenReturn(contatos);
		List<Contato> contatoResult = contatoService.readAllContatos();
		assertEquals(contatoResult, contatos);
	}
	
	@Test
	public void testInsertContato() {
	    // Cria e salva contato e o usuário
		Contato contato = new Contato(4, "Leandro");
	    Usuario usuario = new Usuario();
	    usuario.setId(1); // Defina o id do usuário como Long
	    contato.setUsuario(usuario);

	    // Mocke o resultado do método findById do UsuarioRepository
	    Optional<Usuario> usuarioOpt = Optional.of(usuario);
	    when(usuarioRepository.findById(usuario.getId())).thenReturn(usuarioOpt);

	    // Mocke o resultado do método save do ContatoRepository
	    when(contatoRepository.save(contato)).thenReturn(contato);

	    // Cria um novo serviço ContatoService usando os repositórios mocketados
	    ContatoService contatoService = new ContatoService(contatoRepository, usuarioRepository);

	    // Chame o método insertContato com o objeto Contato
	    Contato contatoResult = contatoService.insertContato(contato);

	    // Verifique se o resultado é igual ao objeto Contato esperado
	    assertEquals(contatoResult.getUsuario().getId(), usuario.getId());
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
