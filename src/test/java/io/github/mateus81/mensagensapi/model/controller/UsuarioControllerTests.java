package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mateus81.mensagensapi.model.dto.UsuarioDTO;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.entity.UsuarioView;
import io.github.mateus81.mensagensapi.model.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTests {

	@InjectMocks
	private UsuarioController usuarioController;

	@Mock
	private UsuarioService usuarioService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new UsuarioController(usuarioService)).build();
	}
	
	@Test
	public void testGetAllUsers() {
		Usuario usuario = new Usuario();
		Usuario usuario2 = new Usuario();
		List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
		List<UsuarioDTO> Dtos = usuarios.stream().map(usuarioEntity -> {
			UsuarioDTO dto = new UsuarioDTO();
			dto.setId(usuarioEntity.getId());
			dto.setNome(usuarioEntity.getNome());
			dto.setEmail(usuarioEntity.getEmail());
			dto.setSenhaNaoProtegida(usuarioEntity.getSenha());
			return dto;
		}).collect(Collectors.toList());
		
		when(usuarioService.getAllUsers()).thenReturn(usuarios);
		List<UsuarioDTO> usuarioResult = usuarioController.getAllUsers();
		assertEquals(usuarioResult, Dtos);
	}
	
	@Test
	public void testGetUserById() throws Exception {
		Usuario usuario = new Usuario(1);
		UsuarioDTO usuarioDto = new UsuarioDTO();
		usuarioDto.setId(usuario.getId());

	    // Mocke o comportamento do método getUserDtoById do UsuarioService
	    when(usuarioService.getUserById(anyInt())).thenReturn(usuario);

	    // Chame o método getUserById com o ID 1
	    UsuarioDTO usuarioResult = usuarioController.getUserById(1);

	    // Verifique se o resultado corresponde ao usuarioDto fictício
	    assertEquals(usuarioResult, usuarioDto);
	}
	
	@Test
	public void testDeleteUserById() {
		Usuario usuario = new Usuario(1, "Marcos");
		doNothing().when(usuarioService).deleteUserById(anyInt());
		usuarioController.deleteUserById(1);
		verify(usuarioService, times(1)).deleteUserById(anyInt());
	}
	
	@Test
	public void testRegisterUser() throws Exception {
		UsuarioDTO usuarioDTO = new UsuarioDTO("Davi", "davi@gmail.com", "senha");
		Usuario usuarioEsperado = new Usuario(1, "Davi", "senha");
		when(usuarioService.registerUser(any(Usuario.class), anyString())).thenReturn(usuarioEsperado);
		
		MvcResult result = mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(usuarioDTO))).andExpect(status().isCreated()).andReturn();
		
		String jsonResult = result.getResponse().getContentAsString();
		Usuario usuarioResult = objectMapper.readerWithView(UsuarioView.Basic.class).readValue(jsonResult, Usuario.class);
		assertEquals(usuarioEsperado.getNome(), usuarioResult.getNome());
		assertEquals(usuarioEsperado.getEmail(), usuarioResult.getEmail());
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		Usuario usuarioAtualizado = new Usuario(1, "Mateus");
		when(usuarioService.getUserById(1)).thenReturn(usuarioAtualizado);
		when(usuarioService.saveOrUpdateUser(usuarioAtualizado)).thenReturn(usuarioAtualizado);
		when(usuarioService.existsById(1)).thenReturn(true);
		Usuario response = usuarioController.updateUser(1, usuarioAtualizado);
		assertEquals(response, usuarioAtualizado);
	}
}
