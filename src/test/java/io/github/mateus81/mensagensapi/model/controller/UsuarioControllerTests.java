package io.github.mateus81.mensagensapi.model.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import io.github.mateus81.mensagensapi.model.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTests {

	@InjectMocks
	private UsuarioController usuarioController;
	
	@Mock
	private UsuarioService usuarioService;
	
	MockMvc mockMvc;
}
