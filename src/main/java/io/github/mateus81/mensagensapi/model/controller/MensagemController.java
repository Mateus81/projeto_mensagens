package io.github.mateus81.mensagensapi.model.controller;

import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;

import io.github.mateus81.mensagensapi.model.dto.MensagemDTO;
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
	public MensagemDTO readMessage(@PathVariable Integer id) {
		// Declara mensagem que será buscada no service e atribui seus valores a seu DTO
		Mensagem mensagem = mensagemService.getMessageById(id);
		if (mensagem != null) {
		MensagemDTO dto = new MensagemDTO();
		dto.setId(mensagem.getId());
		dto.setTexto(mensagem.getTexto());
		dto.setUsuarioRemetente(mensagem.getUsuarioremetente());
		dto.setUsuarioDestino(mensagem.getUsuariodestino());
		dto.setconversa(mensagem.getConversa());
		dto.setVista(mensagem.isVista());
		return dto; 
	} else {
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mensagem não encontrada");
		}
	}
	
	// Exibe mensagem pelo id da conversa
	@GetMapping("/conversas/{conversaId}/mensagens")
	public List<Mensagem> getMensagensByConversaId(@PathVariable Integer conversaId){
		return mensagemService.getMensagensByConversaId(conversaId);
	}

	// Cria/Salva mensagem
	@PostMapping("conversas/{conversaId}/mensagens")
	@ResponseStatus(HttpStatus.CREATED)
	public Mensagem createMessage(@PathVariable Integer conversaId, @RequestBody MensagemDTO dto) throws Exception {
		return mensagemService.createMessage(conversaId, dto);
	}

	// Deleta mensagem
	@DeleteMapping("conversas/{conversaId}/mensagens/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMessageById(@PathVariable Integer id) {
		mensagemService.deleteMessageById(id);
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
	public ResponseEntity<String> markAllAsRead(@PathVariable Integer conversaId) {
		try {
			mensagemService.markAllAsRead(conversaId);
			return ResponseEntity.ok("Todas as mensagens foram marcadas como lidas");
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
