package io.github.mateus81.mensagensapi.model.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.service.ContatoService;

@ExtendWith(MockitoExtension.class)
public class ContatoControllerTests {

	@InjectMocks
	private ContatoController contatoController;
	
	@Mock
	private ContatoService contatoService;
}
