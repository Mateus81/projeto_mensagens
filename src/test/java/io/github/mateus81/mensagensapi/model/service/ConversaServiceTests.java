package io.github.mateus81.mensagensapi.model.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;

@ExtendWith(MockitoExtension.class)
public class ConversaServiceTests {

	@InjectMocks
	private ConversaService conversaService;
	
	@Mock
	private ConversaRepository conversaRepository;
}
