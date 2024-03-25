package io.github.mateus81.mensagensapi.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.github.mateus81.mensagensapi.model.entity.Arquivo;
import io.github.mateus81.mensagensapi.model.repository.ArquivoRepository;

@Service
public class ArquivoService {

	private final ArquivoRepository arquivoRepository;

	public ArquivoService(ArquivoRepository arquivoRepository) {
		this.arquivoRepository = arquivoRepository;
	}

	// Vê todos os arquivos
	public List<Arquivo> readAllArquivo() {
		return arquivoRepository.findAll();
	}

	// Vê arquivo
	@Transactional(readOnly = true)
	public Arquivo readArquivo(Integer arquivoId) {
		return arquivoRepository.findById(arquivoId).orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
	}

	// Envia e salva Arquivo
	@Transactional
	public Arquivo saveArquivo(Integer conversaId, MultipartFile file) {
		// Cria objeto arquivo e busca nome original
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(file.getOriginalFilename());
		// Bloco try/catch
		try {
			arquivo.setConteudo(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arquivoRepository.save(arquivo);
	}

	// Deleta Arquivo
	@Transactional
	public void deleteArquivo(Integer arquivoId) {
		Arquivo arquivo = arquivoRepository.findById(arquivoId)
				.orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
		arquivoRepository.delete(arquivo);
	}
}
