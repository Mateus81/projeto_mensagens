package io.github.mateus81.mensagensapi.model.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.repository.ArquivoRepository;

@ExtendWith(MockitoExtension.class)
public class ArquivoServiceTests {
	
	@InjectMocks
	private ArquivoService arquivoService;
	
	@Mock
	private ArquivoRepository arquivoRepository;
	
}
