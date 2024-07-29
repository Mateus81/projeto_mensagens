package io.github.mateus81.mensagensapi.model.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	// Atributo de verificação
	private static final Logger log = LoggerFactory.getLogger(ConversaService.class);

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
	
	// Mostra todas as conversas onde o Usuário está envolvido (remetente ou destinatário)
	@Transactional(readOnly = true)
	public List<Conversa> readAllConversasByUser(){
		String email = getLoggedUserEmail();
		log.info("Email do usuário logado: {}",email);
		
		Usuario usuario = usuarioRepository.findByEmail(email);
		if(usuario == null) {
			log.error("Usuário não encontrado: {}", email);
			return new ArrayList<>();
		}
		List<Conversa> conversas = conversaRepository.findByUsuarioOrUsuarioDest(usuario, usuario);
		log.info("Conversas encontradas: {}", conversas.size());
		return conversas;
	}
	
	// Lê conversa
	@Transactional(readOnly = true)
	public Conversa readConversaById(Integer conversaId) {
		Conversa conversa = conversaRepository.findById(conversaId)
				.orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
		String email = getLoggedUserEmail();
		if(!conversa.getUsuario().getEmail().equals(email) && !conversa.getUsuarioDest().getEmail().equals(email)) {
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
		if(!conversa.getUsuario().getEmail().equals(email)&& !conversa.getUsuarioDest().getEmail().equals(email)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
		}
		conversaRepository.delete(conversa);
	}

	// Inicia Conversa
	public Conversa startConversa(ConversaDTO conversaDTO) throws Exception {
		// Busca usuario que inicia conversa e busca o contato(usuarioDestino)
		String email = getLoggedUserEmail();
		Usuario usuarioInit = usuarioRepository.findByEmail(email);
		Usuario usuarioDest = usuarioRepository.findByNome(conversaDTO.getUsuarioDest().getNome());
		if(usuarioDest == null) {
			throw new Exception("Usuario não encontrado");
		}
		// Logs de verificação
		System.out.println("Usuário inicial: " + usuarioInit.getNome());
		System.out.println("Usuário destino: " + usuarioDest.getNome());
		
		Conversa conversa = new Conversa();
		conversa.setUsuario(usuarioInit); // Define usuario inicial
		conversa.setUsuarioDest(usuarioDest); // Define usuario destino
		conversa.setStatus(StatusConversa.OPEN);
		conversa.setData_inicio(Date.from(Instant.now()));
		conversa.setMensagens(null);
		// conversa.setData_termino(null);
		return conversaRepository.save(conversa);
	}

	// Finaliza Conversa
	public Conversa endConversa(Integer conversaId) {
		Conversa conversa = conversaRepository.findById(conversaId)
				.orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
		String email = getLoggedUserEmail();
		if(!conversa.getUsuario().getEmail().equals(email) && !conversa.getUsuarioDest().getEmail().equals(email)) {
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
