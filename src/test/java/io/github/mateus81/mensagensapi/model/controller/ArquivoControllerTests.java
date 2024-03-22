package io.github.mateus81.mensagensapi.model.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.service.ArquivoService;

@ExtendWith(MockitoExtension.class)
public class ArquivoControllerTests {

	@InjectMocks
	private ArquivoController arquivoController;
	
	@Mock
	private ArquivoService arquivoService;
}
