package io.github.mateus81.mensagensapi.model.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import io.github.mateus81.mensagensapi.model.dto.ConversaDTO;
import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.entity.Usuario;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;
import io.github.mateus81.mensagensapi.model.repository.UsuarioRepository;

@Service
public class ConversaService {

	private final ConversaRepository conversaRepository;
	private final UsuarioRepository usuarioRepository;

	// Construtor
	public ConversaService(ConversaRepository conversaRepository, UsuarioRepository usuarioRepository) {
		this.conversaRepository = conversaRepository;
		this.usuarioRepository = usuarioRepository;
	}

	// Lê email do usuario logado para operações em conversa
	public String getLoggedUserEmail() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(principal instanceof UserDetails) {
				return ((UserDetails) principal).getUsername();
			} else {
				return principal.toString();
		}
	}
	
	// Mostra todas as conversas do Usuário
	@Transactional(readOnly = true)
	public List<Conversa> readAllConversasByUser(){
		String email = getLoggedUserEmail();
		Usuario usuario = usuarioRepository.findByEmail(email);
		return conversaRepository.findByUsuario(usuario);
	}
	
	// Lê conversa
	@Transactional(readOnly = true)
	public Conversa readConversaById(Integer conversaId) {
		Conversa conversa = conversaRepository.findById(conversaId)
				.orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
		String email = getLoggedUserEmail();
		if(!conversa.getUsuario().getEmail().equals(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
		}
		return conversa;
	}

	// Deleta conversa
	@Transactional
	public void deleteConversaById(Integer conversaId) {
		Conversa conversa = conversaRepository.findById(conversaId)
				.orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
		String email = getLoggedUserEmail();
		if(!conversa.getUsuario().getEmail().equals(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
		}
		conversaRepository.delete(conversa);
	}

	// Inicia Conversa
	public Conversa startConversa(ConversaDTO conversaDTO) throws Exception {
		// Busca usuario que inicia conversa e busca o contato(usuarioDestino)
		String email = getLoggedUserEmail();
		Usuario usuarioInit = usuarioRepository.findByEmail(email);
		Usuario usuarioDest = usuarioRepository.findByNome(conversaDTO.getUsuario().getNome());
		if(usuarioDest == null) {
			throw new Exception("Usuario não encontrado");
		}
		Conversa conversa = new Conversa();
		conversa.setUsuario(usuarioDest);
		conversa.setStatus(StatusConversa.OPEN);
		conversa.setData_inicio(Date.from(Instant.now()));
		// conversa.setData_termino(null);
		return conversaRepository.save(conversa);
	}

	// Finaliza Conversa
	public Conversa endConversa(Integer conversaId) {
		Conversa conversa = conversaRepository.findById(conversaId)
				.orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
		String email = getLoggedUserEmail();
		if(!conversa.getUsuario().getEmail().equals(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
		}
		conversa.setStatus(StatusConversa.CLOSED);
		conversa.setData_termino(Date.from(Instant.now()));
		return conversaRepository.save(conversa);
	}

	// Status da conversa
	public enum StatusConversa {
		OPEN, CLOSED
	}
}
