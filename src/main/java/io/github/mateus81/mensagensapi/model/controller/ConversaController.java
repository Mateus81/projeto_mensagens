package io.github.mateus81.mensagensapi.model.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import io.github.mateus81.mensagensapi.model.dto.ConversaDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.service.ConversaService;

@RestController
@CrossOrigin("*")
public class ConversaController {

	private final ConversaService conversaService;

	public ConversaController(ConversaService conversaService) {
		this.conversaService = conversaService;
	}

	// Exibe todas as conversas
	@GetMapping("/conversas")
	public List<ConversaDTO> getConversasByUser() {
		List<Conversa> conversas = conversaService.readAllConversasByUser();
		return conversas.stream().map(conversa -> {
			ConversaDTO dto = new ConversaDTO();
			dto.setId(conversa.getId());
			dto.setUsuario(conversa.getUsuario());
			return dto;
		}).collect(Collectors.toList());
	}
	
	// Exibe conversa
	@GetMapping("/conversas/{id}")
	public ConversaDTO getConversaById(@PathVariable Integer id) {
		Conversa conversa =  conversaService.readConversaById(id);
		ConversaDTO dto = new ConversaDTO();
		dto.setId(conversa.getId());
		dto.setUsuario(conversa.getUsuario());
		return dto;
	}

	// Deleta conversa
	@DeleteMapping("/conversas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteConversaById(@PathVariable Integer id) {
		conversaService.deleteConversaById(id);
	}

	// Inicia conversa
	@PostMapping("/conversas")
	@ResponseStatus(HttpStatus.CREATED)
	public Conversa startConversa(@RequestBody ConversaDTO novaConversa) {
		Conversa conversa = new Conversa();
		conversa.setId(novaConversa.getId());
		conversa.setUsuario(novaConversa.getUsuario());	
		return conversaService.startConversa(conversa);
	}

	// Termina conversa
	@PutMapping("/conversas/{id}")
	public ResponseEntity<String> endConversa(@PathVariable Integer id) {
		try {
			conversaService.endConversa(id);
			return new ResponseEntity<>("Conversa encerrada com sucesso", HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
