package io.github.mateus81.mensagensapi.model.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import io.github.mateus81.mensagensapi.model.entity.Arquivo;
import io.github.mateus81.mensagensapi.model.service.ArquivoService;

@ExtendWith(MockitoExtension.class)
public class ArquivoControllerTests {

	@InjectMocks
	private ArquivoController arquivoController;

	@Mock
	private ArquivoService arquivoService;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(arquivoController).build();
	}
	
	@Test
	public void testReadAll() {
		// Cria arquivos e adiciona em lista
		Arquivo arquivo = new Arquivo();
		Arquivo arquivo2 = new Arquivo();
		List<Arquivo> arquivos = Arrays.asList(arquivo, arquivo2);
		// Mocka, executa e verifica
		when(arquivoService.readAllArquivo()).thenReturn(arquivos);
		List<Arquivo> arquivoResult = arquivoController.readAll();
		assertEquals(arquivoResult, arquivos);
	}
	
	@Test
	public void testReadArquivo() {
		// Cria arquivo
		Arquivo arquivo = new Arquivo(1, "teste", "txt", "30".getBytes());
		// Mocka, executa e verifica
		when(arquivoService.readArquivoById(anyInt())).thenReturn(arquivo);
		Arquivo arquivoResult = arquivoController.readArquivo(1);
		assertEquals(arquivoResult, arquivo);
	}
	
	@Test
	public void testDeleteArquivo() {
		// Cria arquivo
		Arquivo arquivo = new Arquivo(1, "serei deletado", "txt", "10".getBytes());
		// Deleta e verifica se foi deletado
		doNothing().when(arquivoService).deleteArquivoById(anyInt());
		arquivoController.deleteArquivo(arquivo.getId());
		verify(arquivoService, times(1)).deleteArquivoById(anyInt());
	}
	
	@Test
	public void testSaveArquivo() throws Exception {
		// Cria conteúdo de arquivo
		byte[] mockFileContent = "Conteúdo do arquivo de teste".getBytes();
		// Cria um objeto InputStream
		InputStream mockInputStream = new ByteArrayInputStream(mockFileContent);
		Resource file = new InputStreamResource(mockInputStream);
		MultipartFile multipartFile = new MockMultipartFile("arquivoTeste.txt", file.getInputStream());
		// Cria objeto arquivo e mocka
		Arquivo arquivo = new Arquivo(1, "arquivoTeste.txt", "texto", mockFileContent);
		when(arquivoService.saveArquivo(any(Integer.class), any(MultipartFile.class))).thenReturn(arquivo);
		// Verificações
		ResponseEntity<String> response = arquivoController.saveArquivo(1, multipartFile);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Arquivo enviado com sucesso", response.getBody());
	}
	
	@Test
	public void testDownloadArquivo() throws Exception {
		// Cria arquivo
		Arquivo arquivo = new Arquivo(1, "arquivo.txt", "text/plain", "30".getBytes());
		// Cria byte[]
		byte[] fileContent = arquivo.getConteudo();
		// Verifica se existe o arquivo
		when(arquivoService.downloadArquivo(anyInt())).thenReturn(ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(arquivo.getTipo()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNome() + "\"")
				.body(fileContent));
	
		// Requisição e Resposta
		mockMvc.perform(get("/arquivos/1/download")).andExpect(status().isOk()).andExpect(content().contentType("text/plain"))
		.andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"arquivo.txt\""))
		.andExpect(content().bytes("30".getBytes()));
	}
}
