package io.github.mateus81.mensagensapi.model.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.mateus81.mensagensapi.model.repository.ContatoRepository;

@ExtendWith(MockitoExtension.class)
public class ContatoServiceTests {

	@InjectMocks
	private ContatoService contatoService;

	@Mock
	private ContatoRepository contatoRepository;
}
