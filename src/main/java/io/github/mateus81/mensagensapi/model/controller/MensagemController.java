package io.github.mateus81.mensagensapi.model.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.service.MensagemService;

@CrossOrigin("*")
@RestController
public class MensagemController {

	private final MensagemService mensagemService;

	// Construtor
	public MensagemController(MensagemService mensagemService) {
		this.mensagemService = mensagemService;
	}

	// Exibe mensagem
	@GetMapping("conversas/{conversaId}/mensagens/{id}")
	public Mensagem readMessage(@PathVariable Integer id) {
		return mensagemService.getMessage(id);
	}

	// Cria/Salva mensagem
	@PostMapping("conversas/{conversaId}/mensagens")
	@ResponseStatus(HttpStatus.CREATED)
	public Mensagem createMessage(@RequestBody Mensagem mensagem) {
		return mensagemService.createMessage(mensagem);
	}

	// Deleta mensagem
	@DeleteMapping("conversas/{conversaId}/mensagens/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMessageById(@PathVariable Integer id) {
		mensagemService.deleteMessage(id);
	}

	// Altera mensagem
	@PutMapping("conversas/{conversaId}/mensagens/{id}")
	public ResponseEntity<Mensagem> updateMessage(@PathVariable Integer id, @RequestBody Mensagem mensagemAtualizada) {
		try {
			Mensagem mensagemAtualizar = mensagemService.updateMessage(id, mensagemAtualizada);
			return new ResponseEntity<>(mensagemAtualizar, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// Marca mensagem como lida
	@PatchMapping("conversas/{conversaId}/mensagens/{id}")
	public ResponseEntity<String> markMessageAsRead(@PathVariable Integer id) {
		try {
			mensagemService.markAsRead(id);
			return ResponseEntity.ok("Mensagem marcada como lida");
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// Marca todas como lidas
	@PatchMapping("conversas/{conversaId}/mensagens")
	public ResponseEntity<String> markAllAsRead(@PathVariable Integer id) {
		try {
			mensagemService.markAllAsRead(id);
			return ResponseEntity.ok("Todas as mensagens foram marcadas como lidas");
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
