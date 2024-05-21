package io.github.mateus81.mensagensapi.model.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import io.github.mateus81.mensagensapi.model.entity.Arquivo;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ArquivoRepository;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;

@ExtendWith(MockitoExtension.class)
public class ArquivoServiceTests {

	@InjectMocks
	private ArquivoService arquivoService;

	@Mock
	private ArquivoRepository arquivoRepository;
	
	@Mock
	private ConversaRepository conversaRepository;	
	
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
		Arquivo arquivo = new Arquivo(10);
		when(arquivoRepository.findById(anyInt())).thenReturn(Optional.of(arquivo));
	    // Execução
	    arquivoService.deleteArquivoById(1);
	    // Verificações
	    verify(arquivoRepository).findById(1);
	    verify(arquivoRepository).delete(arquivo);
	}
	
	@Test
	public void testSaveArquivo() throws IOException {
        // Dados de entrada
        String nomeArquivo = "teste.txt";
        byte[] conteudoArquivo = "conteúdo do arquivo".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", nomeArquivo, "text/plain", conteudoArquivo);

        // Configurações dos mocks
        Usuario usuario = new Usuario();
        Conversa conversa = new Conversa(1);
        conversa.setUsuario(usuario);
        when(conversaRepository.findById(conversa.getId())).thenReturn(Optional.of(conversa));

        Arquivo arquivoSalvo = new Arquivo();
        arquivoSalvo.setId(1);
        arquivoSalvo.setNome(nomeArquivo);
        arquivoSalvo.setTipo("txt");
        arquivoSalvo.setConteudo(conteudoArquivo);
        arquivoSalvo.setUsuario(usuario);
        when(arquivoRepository.save(any(Arquivo.class))).thenReturn(arquivoSalvo);

        // Chama o método a ser testado
        Arquivo savedArquivo = arquivoService.saveArquivo(conversa.getId(), file);

        // Verificações
        assertNotNull(savedArquivo, "O arquivo salvo não deve ser nulo");
        assertEquals(nomeArquivo, savedArquivo.getNome());
        assertEquals("txt", savedArquivo.getTipo());
        assertArrayEquals(conteudoArquivo, savedArquivo.getConteudo());
        assertEquals(usuario, savedArquivo.getUsuario());

        // Verifica se os métodos dos repositórios foram chamados corretamente
        verify(conversaRepository).findById(conversa.getId());
        verify(arquivoRepository).save(any(Arquivo.class));
    }
}
