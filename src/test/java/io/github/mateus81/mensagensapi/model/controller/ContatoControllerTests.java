package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.mateus81.mensagensapi.model.dto.ContatoDTO;
import io.github.mateus81.mensagensapi.model.entity.Contato;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;
import io.github.mateus81.mensagensapi.model.service.ContatoService;

@ExtendWith(MockitoExtension.class)
public class ContatoControllerTests {

	@InjectMocks
	private ContatoController contatoController;

	@Mock
	private ContatoService contatoService;
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Test
	public void testGetAllContatos() {
		Usuario usuario = new Usuario(1);
		Contato contato = new Contato();
		contato.setUsuario(usuario);
		Contato contato2 = new Contato();
		List<Contato> contatos = Arrays.asList(contato, contato2);
		List<ContatoDTO> Dtos = contatos.stream().map(contatoEntity -> {
			ContatoDTO dto = new ContatoDTO();
			dto.setId(contatoEntity.getId());
			dto.setNome(contatoEntity.getNome());
			dto.setEmail(contatoEntity.getEmail());
			dto.setTelefone(contatoEntity.getTelefone());
			dto.setUsuario(contatoEntity.getUsuario());
			return dto;
		}).collect(Collectors.toList());
		
		when(contatoService.readContatosByUsuario(1)).thenReturn(contatos);
		List<ContatoDTO> contatoResult = contatoController.getContatosByUsuario(1);
		assertEquals(contatoResult, Dtos);
	}
	
	@Test
	public void testGetContatoById() {
		// Cria contato
		Contato contato = new Contato(1, "Matt");
		ContatoDTO contatoDto = new ContatoDTO();
		contatoDto.setId(contato.getId());
		contatoDto.setNome(contato.getNome());
		// Valida
		when(contatoService.readContatoById(anyInt())).thenReturn(contato);
		ContatoDTO contatoResult = contatoController.getContato(1);
		assertEquals(contatoResult, contatoDto);
	}
	
	@Test
	public void testDeleteContatoById() {
		Contato contato = new Contato(1, "James");
		doNothing().when(contatoService).deleteContatoById(anyInt());
		contatoController.deleteContatoById(3);
		verify(contatoService, times(1)).deleteContatoById(anyInt());
	}
	
	@Test
	public void testInsertContato() {
		 // Crie um objeto ContatoDTO
	    ContatoDTO contatoDto = new ContatoDTO();
	    contatoDto.setId(1);
	    contatoDto.setNome("Felipe");
	    contatoDto.setEmail("Felipe@gmail.com");

	    // Crie um objeto Contato
	    Usuario usuarioContato = new Usuario();
	    usuarioContato.setId(contatoDto.getId());
	    usuarioContato.setNome(contatoDto.getNome());
	    usuarioContato.setEmail(contatoDto.getEmail());
	    Usuario usuarioAssociado = new Usuario();
	    usuarioAssociado.setId(1); // Use o id do usuário
	    usuarioAssociado.setContatos(new ArrayList<>());

	    // Mocks
	    when(contatoService.insertContato(eq(1), any(Usuario.class))).thenReturn(usuarioContato);
	    
	    // Chame o método insertContato com o objeto ContatoDTO
	    Usuario contatoResult = contatoController.insertContato(1, contatoDto);

	    // Verifique se o resultado é igual ao objeto esperado
	    verify(contatoService).insertContato(eq(1), any(Usuario.class));
	    assertEquals(contatoResult.getId(), usuarioContato.getId());
	    assertEquals(contatoResult.getNome(), usuarioContato.getNome());
	    assertEquals(contatoResult.getEmail(), usuarioContato.getEmail());
	}
	
	@Test 
	public void testUpdateContato() {
		Contato contatoAtualizado = new Contato(1, "Tomás de Aquino");
		when(contatoService.updateContato(1, contatoAtualizado)).thenReturn(contatoAtualizado);
		ResponseEntity<Contato> response = contatoController.updateContato(1, contatoAtualizado);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(contatoAtualizado, response.getBody());
	}
}
