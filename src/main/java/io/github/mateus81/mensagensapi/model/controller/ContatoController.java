package io.github.mateus81.mensagensapi.model.controller;

import java.util.List;

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

import io.github.mateus81.mensagensapi.model.entity.Contato;
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
	public List<Contato> getAllContatos() {
		return contatoService.readAllContatos();
	}

	// Vê um contato
	@GetMapping("/contatos/{id}")
	public Contato getContato(Integer id) {
		return contatoService.readContato(id);
	}

	// Exclui contato
	@DeleteMapping("/contatos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteContatoById(@PathVariable Integer id) {
		contatoService.deleteContato(id);
	}

	// Insere contato
	@PostMapping("/contatos")
	@ResponseStatus(HttpStatus.CREATED)
	public Contato insertContato(@RequestBody Contato contato) {
		return contatoService.insertContato(contato);
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
