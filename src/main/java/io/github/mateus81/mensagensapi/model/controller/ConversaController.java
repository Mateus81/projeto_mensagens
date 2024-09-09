package io.github.mateus81.mensagensapi.model.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import io.github.mateus81.mensagensapi.model.dto.MensagemDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Mensagem;
import io.github.mateus81.mensagensapi.model.service.ConversaService;

@RestController
@CrossOrigin("*")
public class ConversaController {

	private final ConversaService conversaService;

	public ConversaController(ConversaService conversaService) {
		this.conversaService = conversaService;
	}
	
	// Método que serializa mensagemDTO
	public MensagemDTO convertToDto(Mensagem mensagem) {
		MensagemDTO dto = new MensagemDTO();
		dto.setId(mensagem.getId());
		dto.setTexto(mensagem.getTexto());
		dto.setUsuarioRemetente(mensagem.getUsuarioRemetente());
		dto.setUsuarioDestino(mensagem.getUsuarioDestino());
		dto.setConversa(mensagem.getConversa());
		dto.setVista(mensagem.isVista());
		return dto;
	}
	
	// Método que chama o outro em lista
    public List<MensagemDTO> convertToDTO(List<Mensagem> mensagens) {
        return mensagens.stream().map(this::convertToDto).collect(Collectors.toList());
    }

	// Exibe todas as conversas do usuário
	@GetMapping("/conversas")
	@Transactional // Evita erro de serialização em mensagens, pois o atributo tem @Lob
	public List<ConversaDTO> getConversasByUser() {
		List<Conversa> conversas = conversaService.readAllConversasByUser();
		return conversas.stream().map(conversa -> {
			ConversaDTO dto = new ConversaDTO();
			dto.setId(conversa.getId());
			dto.setUsuario(conversa.getUsuario());
			dto.setUsuarioDest(conversa.getUsuarioDest());
			dto.setMensagens(convertToDTO(conversa.getMensagens()));
			return dto;
		}).collect(Collectors.toList());
	}
	
	// Exibe conversa
	@GetMapping("/conversas/{id}")
	@Transactional // Evita erro de serialização em mensagens, pois o atributo tem @Lob
	public ConversaDTO getConversaById(@PathVariable Integer id) {
		Conversa conversa =  conversaService.readConversaById(id);
		ConversaDTO dto = new ConversaDTO();
		dto.setId(conversa.getId());
		dto.setUsuario(conversa.getUsuario());
		dto.setUsuarioDest(conversa.getUsuarioDest());
		dto.setMensagens(convertToDTO(conversa.getMensagens()));
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
	public Conversa startConversa(@RequestBody ConversaDTO novaConversa) throws Exception {
		return conversaService.startConversa(novaConversa);
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
