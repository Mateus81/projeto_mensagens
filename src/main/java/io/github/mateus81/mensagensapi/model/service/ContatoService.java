package io.github.mateus81.mensagensapi.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mateus81.mensagensapi.model.entity.Contato;
import io.github.mateus81.mensagensapi.model.repository.ContatoRepository;

@Service
public class ContatoService {

	private final ContatoRepository contatoRepository;
	
	// Construtor
	public ContatoService(ContatoRepository contatoRepository) {
		this.contatoRepository = contatoRepository;
	}
	// Vê Contato
	public Contato readContato(Integer contatoId) {
		return contatoRepository.findById(contatoId).orElseThrow(() -> new RuntimeException("Contato não encontrado"));
	}
	// Vê lista de contatos
	public List<Contato> readAllContatos(){
		return contatoRepository.findAll();
	}
	// Inclui Contato
	@Transactional
	public Contato insertContato(Contato contato) {
		return contatoRepository.save(contato);
	}
	// Excluir Contato
	@Transactional
	public void deleteContato(Integer contatoId) {
		if(!contatoRepository.existsById(contatoId)) {
			throw new RuntimeException("Contato não encontrado");
		}
		contatoRepository.deleteById(contatoId);
	}
	
	// Atualiza contato
	@Transactional
	public Contato updateContato(Integer id, Contato contatoAtualizado) {
		Contato contatoExistente = contatoRepository.findById(id).orElseThrow(() -> new RuntimeException("Contato não encontrado"));
		contatoAtualizado.setId(id);
		contatoExistente.setNome(contatoAtualizado.getNome());
		contatoExistente.setEmail(contatoAtualizado.getEmail());
		
		Contato contatoSalvo = contatoRepository.save(contatoExistente);
		return contatoSalvo;
	}
}
