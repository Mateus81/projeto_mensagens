package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import io.github.mateus81.mensagensapi.model.entity.Arquivo;
import io.github.mateus81.mensagensapi.model.repository.ArquivoRepository;

@ExtendWith(MockitoExtension.class)
public class ArquivoServiceTests {

	@InjectMocks
	private ArquivoService arquivoService;

	@Mock
	private ArquivoRepository arquivoRepository;
	
	@Mock
	private MultipartFile file;

	@Test
	public void testReadArquivoById() {
		Arquivo arquivo = new Arquivo(1, "arquivo", "txt", "20".getBytes());
		when(arquivoRepository.findById(anyInt())).thenReturn(Optional.of(arquivo));
		Arquivo arquivoResult = arquivoService.readArquivoById(1);
		assertEquals(arquivoResult, arquivo);
	}
	
	@Test
	public void testReadAllArquivo() {
		Arquivo arquivo = new Arquivo();
		Arquivo arquivo2 = new Arquivo();
		List<Arquivo> arquivos = Arrays.asList(arquivo, arquivo2);
		when(arquivoRepository.findAll()).thenReturn(arquivos);
		List<Arquivo> arquivoResult = arquivoService.readAllArquivo();
		assertEquals(arquivoResult, arquivos);
	}
	
	@Test
	public void testDeleteArquivoById() {
		Arquivo arquivo = new Arquivo(1, "foto", "png", "40".getBytes());
		when(arquivoRepository.findById(anyInt())).thenReturn(Optional.of(arquivo));
		arquivoService.deleteArquivoById(1);
		verify(arquivoRepository).delete(arquivo);
	}
	
	@Test
	public void testSaveArquivo() throws IOException {
		// Declaração de variáveis
		Integer conversaId = 1;
		String nomeArquivo = "meu arquivo";
		byte[] conteudoArquivo = "Teste de arquivo".getBytes();
		// Operações
		when(file.getOriginalFilename()).thenReturn(nomeArquivo);
		when(file.getBytes()).thenReturn(conteudoArquivo);
		
		assertNotNull(arquivoService);
		assertNotNull(arquivoRepository); 
		
		ReflectionTestUtils.setField(arquivoService, "arquivoRepository", arquivoRepository);
		arquivoService.saveArquivo(conversaId, file);	
		ArgumentCaptor<Arquivo> argument = ArgumentCaptor.forClass(Arquivo.class);
		verify(arquivoRepository).save(argument.capture());
		
		Arquivo capturedArquivo = argument.getValue();
		// Verifica	
		assertNotNull(capturedArquivo);
		assertEquals(nomeArquivo, capturedArquivo.getNome());
		assertArrayEquals(conteudoArquivo, capturedArquivo.getConteudo());
		assertEquals(conteudoArquivo.length, capturedArquivo.getTamanho());
		
	}
}
