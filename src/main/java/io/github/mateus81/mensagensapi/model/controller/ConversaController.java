package io.github.mateus81.mensagensapi.model.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.service.ConversaService;

@RestController
@CrossOrigin
public class ConversaController {

	private final ConversaService conversaService;
	
	public ConversaController(ConversaService conversaService) {
		this.conversaService = conversaService;
	}
	
	// Exibe conversa
	@GetMapping("/conversas/{id}")
	public Conversa getConversaById(Integer id) {
		return conversaService.readConversa(id);
	}
	
	// Deleta conversa
	@DeleteMapping("/conversas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteConversaById(Integer id) {
		conversaService.deleteConversa(id);
	}
	
	// Inicia conversa
	@PostMapping("/conversas")
	@ResponseStatus(HttpStatus.CREATED)
	public Conversa startConversa(@RequestBody Conversa novaConversa) {
		return conversaService.startConversa(novaConversa);
	}
	
	// Termina conversa
	@PutMapping("/conversas/{id}")
	public ResponseEntity<String> endConversa(@PathVariable Integer id) {
		try {
			conversaService.endConversa(id);
			return new ResponseEntity<>("Conversa encerrada com sucesso", HttpStatus.OK);
		} catch(RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
