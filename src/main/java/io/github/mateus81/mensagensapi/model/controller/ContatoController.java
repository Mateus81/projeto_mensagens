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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.github.mateus81.mensagensapi.model.dto.ContatoDTO;
import io.github.mateus81.mensagensapi.model.entity.Contato;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.service.ContatoService;

@CrossOrigin("*")
@RestController
public class ContatoController {

	private final ContatoService contatoService;

	// Construtor
	public ContatoController(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	// Vê lista de contatos
	@GetMapping("/contatos")
	public List<ContatoDTO> getContatosByUsuario(@RequestParam Integer usuarioId) {
		List<Contato> contatos = contatoService.readContatosByUsuario(usuarioId);
		return contatos.stream().map(contato -> {
			ContatoDTO dto = new ContatoDTO();
			dto.setId(contato.getId());
			dto.setNome(contato.getNome());
			dto.setEmail(contato.getEmail());
			dto.setTelefone(contato.getTelefone());
			dto.setUsuario(contato.getUsuario());
			return dto;
		}).collect(Collectors.toList());
	}

	// Vê um contato
	@GetMapping("/contatos/{id}")
	public ContatoDTO getContato(@PathVariable Integer id) {
		Contato contato = contatoService.readContatoById(id);
		ContatoDTO dto = new ContatoDTO();
		dto.setId(contato.getId());
		dto.setNome(contato.getNome());
		dto.setEmail(contato.getEmail());
		dto.setTelefone(contato.getTelefone());
		dto.setUsuario(contato.getUsuario());
		return dto;
	}

	// Exclui contato
	@DeleteMapping("/contatos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteContatoById(@PathVariable Integer id) {
		contatoService.deleteContatoById(id);
	}

	// Insere contato
	@PostMapping("/contatos/{usuarioAssociadoId}")
	@ResponseStatus(HttpStatus.CREATED)
	public Usuario insertContato(@PathVariable Integer usuarioAssociadoId, @RequestBody ContatoDTO contatoDto) {
		Usuario contato = new Usuario();
		contato.setId(contatoDto.getId());
		contato.setNome(contatoDto.getNome());
		contato.setEmail(contatoDto.getEmail());
		
		return contatoService.insertContato(usuarioAssociadoId, contato);
	}

	// Atualiza contato
	@PutMapping("/contatos/{id}")
	public ResponseEntity<Contato> updateContato(@PathVariable Integer id, @RequestBody Contato contatoAtualizado) {
		try {
			Contato contatoAtualizar = contatoService.updateContato(id, contatoAtualizado);
			return new ResponseEntity<>(contatoAtualizar, HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
