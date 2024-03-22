package io.github.mateus81.mensagensapi.model.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.repository.MensagemRepository;

@ExtendWith(MockitoExtension.class)
public class MensagemServiceTests {

	@InjectMocks
	private MensagemService mensagemService;
	
	@Mock
	private MensagemRepository mensagemRepository;
}
