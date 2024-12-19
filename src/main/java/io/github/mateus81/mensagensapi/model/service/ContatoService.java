package io.github.mateus81.mensagensapi.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mateus81.mensagensapi.model.dto.ContatoDTO;
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

	@Transactional
	public Contato insertContato(Integer usuarioAssociadoId, ContatoDTO contatoDto) {
		// Busca usuário associado
		Optional<Usuario> usuarioAssociadoOpt = usuarioRepository.findById(usuarioAssociadoId);
		if(!usuarioAssociadoOpt.isPresent()) {
			throw new RuntimeException("Usuário associado não encontrado");
		}
		// Verifica se Contato existe no banco de dados como um usuário
		Optional<Usuario> contatoComoUsuario = usuarioRepository.findOptionalByNome(contatoDto.getNome());
		if(!contatoComoUsuario.isPresent()) {
			throw new RuntimeException("Contato não é um usuário existente");
		}
		
		Usuario usuarioAssociado = usuarioAssociadoOpt.get();
		
		// Impede de adicionar duas vezes o mesmo contato
		if(contatoRepository.existsByUsuarioIdAndNome(usuarioAssociadoId, contatoDto.getNome())){
			throw new RuntimeException("Contato já está adicionado");
		}
		// Definindo contato
		Contato contato = new Contato();
		contato.setId(contatoDto.getId());
		contato.setNome(contatoDto.getNome());
		contato.setEmail(contatoDto.getEmail());
		contato.setTelefone(contatoDto.getTelefone());
		contato.setUsuario(usuarioAssociado);
		return contatoRepository.save(contato);
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
		contatoExistente.setNome(Optional.ofNullable(contatoAtualizado.getNome()).orElse(contatoExistente.getNome()));

		Contato contatoSalvo = contatoRepository.save(contatoExistente);
		return contatoSalvo;
	}
}
