package io.github.mateus81.mensagensapi.model.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {

	@InjectMocks
	private UsuarioService usuarioService;

	@Mock
	private UsuarioRepository usuarioRepository;
}
