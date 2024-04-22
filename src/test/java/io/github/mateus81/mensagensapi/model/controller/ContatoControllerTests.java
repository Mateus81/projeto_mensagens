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

import io.github.mateus81.mensagensapi.model.dto.ContatoDTO;
import io.github.mateus81.mensagensapi.model.dto.UsuarioDTO;
import io.github.mateus81.mensagensapi.model.entity.Contato;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.ContatoService;

@ExtendWith(MockitoExtension.class)
public class ContatoControllerTests {

	@InjectMocks
	private ContatoController contatoController;

	@Mock
	private ContatoService contatoService;
	
	@Test
	public void testGetAllContatos() {
		Contato contato = new Contato();
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
		
		when(contatoService.readAllContatos()).thenReturn(contatos);
		List<ContatoDTO> contatoResult = contatoController.getAllContatos();
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
	    contatoDto.setNome("Felipe");
	    contatoDto.setEmail("Felipe@gmail.com");
	    contatoDto.setTelefone("123456789");
	    contatoDto.setUsuario(null);

	    // Crie um objeto Contato
	    Contato contato = new Contato();
	    contato.setNome(contatoDto.getNome());
	    contato.setEmail(contatoDto.getEmail());
	    contato.setTelefone(contatoDto.getTelefone());
	    Usuario usuario = new Usuario();
	    usuario.setId(1); // Use o id do usuário
	    contato.setUsuario(usuario);

	    // Mocke o serviço ContatoService
	    when(contatoService.insertContato(any(Contato.class))).thenReturn(contato);
	    
	    // Chame o método insertContato com o objeto ContatoDTO
	    Contato contatoResult = contatoController.insertContato(contatoDto);

	    // Verifique se o resultado é igual ao objeto Contato esperado
	    assertEquals(contatoResult, contato);
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
