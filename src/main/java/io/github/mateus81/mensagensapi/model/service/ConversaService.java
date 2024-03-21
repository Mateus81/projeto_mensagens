package io.github.mateus81.mensagensapi.model.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mateus81.mensagensapi.model.entity.Conversa;
import io.github.mateus81.mensagensapi.model.repository.ConversaRepository;

@Service
public class ConversaService {

	private final ConversaRepository conversaRepository;
	
	// Construtor
	public ConversaService(ConversaRepository conversaRepository){
		this.conversaRepository = conversaRepository;
	}
	
	// Lê conversa
	@Transactional(readOnly = true)
	public Conversa readConversa(Integer conversaId) {
		return conversaRepository.findById(conversaId).orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
	}
	
	// Deleta conversa
	@Transactional
	public void deleteConversa(Integer conversaId) {
		Conversa conversa = conversaRepository.findById(conversaId).orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
		conversaRepository.delete(conversa);
	}
	
	// Inicia Conversa
	public Conversa startConversa(Conversa conversa) {
		conversa.setStatus(StatusConversa.OPEN);
		conversa.setData_inicio(LocalDateTime.now());
		// conversa.setData_termino(null);
		return conversaRepository.save(conversa);
	}
	
	// Finaliza Conversa
	public Conversa endConversa(Integer conversaId) {
		Conversa conversa = conversaRepository.findById(conversaId).orElseThrow(() -> new RuntimeException("Conversa não encontrada"));
		conversa.setStatus(StatusConversa.CLOSED);
		conversa.setData_termino(LocalDateTime.now());
		return conversaRepository.save(conversa);
	}
	
	// Status da conversa
	public enum StatusConversa {
		OPEN,
		CLOSED
	}
}
