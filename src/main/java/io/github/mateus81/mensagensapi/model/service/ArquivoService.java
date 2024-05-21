package io.github.mateus81.mensagensapi.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.github.mateus81.mensagensapi.model.entity.Arquivo;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ArquivoRepository;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;

@Service
public class ArquivoService {

	private final ArquivoRepository arquivoRepository;
	
	private final ConversaRepository conversaRepository;
	

	public ArquivoService(ArquivoRepository arquivoRepository, ConversaRepository conversaRepository) {
		this.arquivoRepository = arquivoRepository;
		this.conversaRepository = conversaRepository;
	}

	// Vê todos os arquivos
	public List<Arquivo> readAllArquivo() {
		return arquivoRepository.findAll();
	}

	// Vê arquivo
	@Transactional(readOnly = true)
	public Arquivo readArquivoById(Integer arquivoId) {
		return arquivoRepository.findById(arquivoId).orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
	}

	// Envia e salva Arquivo
	@Transactional
	public Arquivo saveArquivo(Integer conversaId, MultipartFile file) {
		// Obtém o usuário e busca a conversa
	    Conversa conversa = conversaRepository.findById(conversaId)
	    		.orElseThrow(() -> new IllegalArgumentException("Conversa não encontrada"));
	    Usuario usuario = conversa.getUsuario();
		// Cria objeto arquivo e busca nome, tipo, conteúdo e usuario originais
		Arquivo arquivo = new Arquivo();
		arquivo.setNome(file.getOriginalFilename());
		String tipo = StringUtils.getFilenameExtension(file.getOriginalFilename());
		arquivo.setTipo(tipo);
		arquivo.setUsuario(usuario);
		// Bloco try/catch
		try {
			arquivo.setConteudo(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Arquivo arquivoSalvo = arquivoRepository.save(arquivo);
		return arquivoSalvo;
	}

	// Deleta Arquivo
	@Transactional
	public void deleteArquivoById(Integer arquivoId) {
		Optional<Arquivo> arquivo = arquivoRepository.findById(arquivoId);
		if (arquivo.isPresent()) {
			arquivoRepository.delete(arquivo.get());
		} else {
			throw new RuntimeException("Arquivo não encontrado");
		}
	}
}
