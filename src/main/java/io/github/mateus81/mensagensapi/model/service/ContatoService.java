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

	// Vê lista de contatos do usuário logado
	public List<Contato> readContatosByUsuario(Integer usuarioId) {
		return contatoRepository.findByUsuarioId(usuarioId);
	}

	// Inclui Contato
	@Transactional
	public Usuario insertContato(Integer usuarioAssociadoId, Usuario contato) {
		// Busca usuário associado
		Optional<Usuario> usuarioAssociadoOpt = usuarioRepository.findById(usuarioAssociadoId);
		if(!usuarioAssociadoOpt.isPresent()) {
			throw new RuntimeException("Usuário associado não encontrado");
		}
		// Verifica se Contato existe no banco de dados como um usuário
		Optional<Usuario> contatoComoUsuario = usuarioRepository.findOptionalByNome(contato.getNome());
		if(!contatoComoUsuario.isPresent()) {
			throw new RuntimeException("Contato não é um usuário existente");
		}
	
		Usuario usuarioAssociado = usuarioAssociadoOpt.get();
		Usuario usuarioContato = contatoComoUsuario.get();
		// Impede de adicionar duas vezes o mesmo contato
		if(usuarioAssociado.getContatos().stream().anyMatch(c -> c.getNome().equals(contato.getNome()))){
			throw new RuntimeException("Contato já está adicionado");
		}
		
		usuarioAssociado.getContatos().add(usuarioContato);
		usuarioRepository.save(usuarioAssociado);
		return usuarioContato;
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

		Contato contatoSalvo = contatoRepository.save(contatoExistente);
		return contatoSalvo;
	}
}
