package io.github.mateus81.mensagensapi.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mateus81.mensagensapi.model.entity.Contato;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ContatoRepository;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@Service
public class ContatoService {

	private final ContatoRepository contatoRepository;
	
	private final UsuarioRepository usuarioRepository;
	
	
	// Construtor
	public ContatoService(ContatoRepository contatoRepository, UsuarioRepository usuarioRepository) {
		this.contatoRepository = contatoRepository;
		this.usuarioRepository = usuarioRepository;
	}

	// Vê Contato
	public Contato readContatoById(Integer contatoId) {
		return contatoRepository.findById(contatoId).orElseThrow(() -> new RuntimeException("Contato não encontrado"));
	}

	// Vê lista de contatos
	public List<Contato> readAllContatos() {
		return contatoRepository.findAll();
	}

	// Inclui Contato
	@Transactional
	public Contato insertContato(Contato contato) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(contato.getUsuario().getId());
		if(usuarioOpt.isPresent()) {
			Usuario usuario = usuarioOpt.get();
			contato.setUsuario(usuario);
			return contatoRepository.save(contato);
		} else {
			throw new RuntimeException("Usuário não encontrado");
		}
		
	}

	// Excluir Contato
	@Transactional
	public void deleteContatoById(Integer contatoId) {
		Contato contato = contatoRepository.findById(contatoId).orElseThrow(() -> new RuntimeException("Contato não encontrado"));
		contatoRepository.delete(contato);
	}

	// Atualiza contato
	@Transactional
	public Contato updateContato(Integer id, Contato contatoAtualizado) {
		Contato contatoExistente = contatoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Contato não encontrado"));
		contatoAtualizado.setId(id);
		contatoExistente.setNome(contatoAtualizado.getNome());
		contatoExistente.setEmail(contatoAtualizado.getEmail());

		Contato contatoSalvo = contatoRepository.save(contatoExistente);
		return contatoSalvo;
	}
}
