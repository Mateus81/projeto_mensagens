package io.github.mateus81.mensagensapi.model.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.mateus81.mensagensapi.model.entity.Arquivo;
import io.github.mateus81.mensagensapi.model.service.ArquivoService;

@CrossOrigin("*")
@RestController
public class ArquivoController {

	private final ArquivoService arquivoService;

	// Construtor
	public ArquivoController(ArquivoService arquivoService) {
		this.arquivoService = arquivoService;
	}

	// Retorna lista de arquivos
	@GetMapping("/arquivos")
	public List<Arquivo> readAll() {
		return arquivoService.readAllArquivo();
	}

	// Retorna um arquivo
	@GetMapping("/arquivos/{id}")
	public Arquivo readArquivo(@PathVariable Integer id) {
		return arquivoService.readArquivoById(id);
	}

	// Deleta arquivo
	@DeleteMapping("/arquivos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteArquivo(@PathVariable Integer id) {
		arquivoService.deleteArquivoById(id);
	}

	// Salva e envia arquivo
	@PostMapping("/conversas/{id}/arquivos")
	public ResponseEntity<String> saveArquivo(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
		Arquivo arquivoSalvo = arquivoService.saveArquivo(id, file);
		if (arquivoSalvo != null) {
			return ResponseEntity.ok("Arquivo enviado com sucesso");
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao enviar o arquivo");
		}
	}
}
