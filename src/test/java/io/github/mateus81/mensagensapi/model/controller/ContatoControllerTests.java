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
		when(contatoService.readAllContatos()).thenReturn(contatos);
		List<Contato> contatoResult = contatoController.getAllContatos();
		assertEquals(contatoResult, contatos);
	}
	
	@Test
	public void testGetContatoById() {
		Contato contato = new Contato(1, "Matt");
		when(contatoService.readContatoById(anyInt())).thenReturn(contato);
		Contato contatoResult = contatoController.getContato(1);
		assertEquals(contatoResult, contato);
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
	    contatoDto.setUsuario(1); // Use o id do usuário

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
