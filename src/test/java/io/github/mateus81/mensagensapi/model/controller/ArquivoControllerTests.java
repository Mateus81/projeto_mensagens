package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import io.github.mateus81.mensagensapi.model.entity.Arquivo;
import io.github.mateus81.mensagensapi.model.service.ArquivoService;

@ExtendWith(MockitoExtension.class)
public class ArquivoControllerTests {

	@InjectMocks
	private ArquivoController arquivoController;

	@Mock
	private ArquivoService arquivoService;
	
	@Test
	public void testReadAll() {
		Arquivo arquivo = new Arquivo();
		Arquivo arquivo2 = new Arquivo();
		List<Arquivo> arquivos = Arrays.asList(arquivo, arquivo2);
		when(arquivoService.readAllArquivo()).thenReturn(arquivos);
		List<Arquivo> arquivoResult = arquivoController.readAll();
		assertEquals(arquivoResult, arquivos);
	}
	
	@Test
	public void testReadArquivo() {
		Arquivo arquivo = new Arquivo(1, "teste", "txt", "30".getBytes());
		when(arquivoService.readArquivoById(anyInt())).thenReturn(arquivo);
		Arquivo arquivoResult = arquivoController.readArquivo(1);
		assertEquals(arquivoResult, arquivo);
	}
	
	@Test
	public void testDeleteArquivo() {
		Arquivo arquivo = new Arquivo(1, "serei deletado", "txt", "10".getBytes());
		doNothing().when(arquivoService).deleteArquivoById(anyInt());
		arquivoController.deleteArquivo(1);
		verify(arquivoService, times(1)).deleteArquivoById(anyInt());
	}
	
	@Test
	public void testSaveArquivo() throws Exception {
		byte[] mockFileContent = "Conteúdo do arquivo de teste".getBytes();
		InputStream mockInputStream = new ByteArrayInputStream(mockFileContent);
		Resource file = new InputStreamResource(mockInputStream);
		MultipartFile multipartFile = new MockMultipartFile("arquivoTeste.txt", file.getInputStream());
		
		Arquivo arquivo = new Arquivo(1, "arquivoTeste.txt", "texto", mockFileContent);
		when(arquivoService.saveArquivo(any(Integer.class), any(MultipartFile.class))).thenReturn(arquivo);
		
		ResponseEntity<String> response = arquivoController.saveArquivo(1, multipartFile);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Arquivo enviado com sucesso", response.getBody());
	}
}
